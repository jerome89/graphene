package com.graphene.reader.store.data.cassandra.handler

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.PreparedStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.ResultSetFuture
import com.datastax.driver.core.Session
import com.google.common.base.Function
import com.google.common.base.Joiner
import com.google.common.base.Stopwatch
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.google.gson.Gson
import com.graphene.common.beans.OffsetRange
import com.graphene.common.beans.Path
import com.graphene.common.beans.QueryRange
import com.graphene.common.store.data.cassandra.CassandraFactory
import com.graphene.common.store.data.cassandra.property.CassandraDataHandlerProperty
import com.graphene.reader.beans.TimeSeries
import com.graphene.reader.exceptions.TooMuchDataExpectedException
import com.graphene.reader.service.metric.DataFetchHandler
import com.graphene.reader.store.data.DataFetchHandlerProperty
import com.graphene.reader.utils.Jsons
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.apache.logging.log4j.LogManager

/**
 * @author Andrei Ivanov
 * @author dark
 * @author jerome89
 */
class OffsetBasedDataFetchHandler(
  cassandraFactory: CassandraFactory,
  dataFetchHandlerProperty: DataFetchHandlerProperty
) : DataFetchHandler {

  private val query: String = """
    SELECT offset, data
        FROM ${dataFetchHandlerProperty.keyspace}.${dataFetchHandlerProperty.columnFamily}
        WHERE path = ?
              AND tenant = ?
              AND startTime = ?
              AND offset >= ?
              AND offset <= ?
        ORDER BY offset;"""

  private var cluster: Cluster
  private var session: Session
  private var statement: PreparedStatement
  private var rollup: Int = 60
  private var maxPoints: Int = Int.MAX_VALUE
  private var bucketSize: Int = 604800

  init {
    val property = Jsons.from(dataFetchHandlerProperty.handler["property"], CassandraDataHandlerProperty::class.java)
    this.cluster = cassandraFactory.createCluster(property)
    this.session = cluster.connect()
    this.statement = session.prepare(query)
    this.rollup = dataFetchHandlerProperty.rollup
    this.maxPoints = dataFetchHandlerProperty.maxPoints
    this.bucketSize = dataFetchHandlerProperty.bucketSize
  }

  private fun executeAsync(path: String, tenant: String, startTime: Long, startOffset: Short, endOffset: Short): ResultSetFuture {
    return session.executeAsync(
      statement.bind(
        path,
        tenant,
        startTime,
        startOffset,
        endOffset
      )
    )
  }

  private val executorService: ExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool())
  @Throws(ExecutionException::class, InterruptedException::class, TooMuchDataExpectedException::class)
  override fun getMetricsAsJson(tenant: String, paths: List<Path>, from: Long, to: Long): String { // Calculate rollup etc
    val queryRange = QueryRange(from, to, rollup)
    val queryOffsetRanges = createQueryOffsetRanges(queryRange)
    logger.debug(queryRange.toString())
    logger.debug("Expected number of series is ${paths.size}")
    if (paths.size * queryRange.expectedCount > maxPoints) {
      logger.debug("Expected total number of data points exceeds the limit: ${paths.size * queryRange.expectedCount}")
      throw TooMuchDataExpectedException("Expected total number of data points exceeds the limit: ${paths.size * queryRange.expectedCount} (the limit is $maxPoints)")
    }
    // Now let's query C*
    var futures: MutableList<ListenableFuture<PartialPathResult>> = Lists.newArrayListWithExpectedSize(paths.size * queryOffsetRanges.keys.size)
    for (path in paths) {
      for ((startTime, offsetRange) in queryOffsetRanges) {
        val serializeFunction = Function<ResultSet, PartialPathResult> {
          resultSet: ResultSet? ->
          val result = PartialPathResult(path, startTime, offsetRange)
          result.makeJson(resultSet)
          result
        }
        futures.add(
          Futures.transform(
            executeAsync(path.path, tenant, startTime, offsetRange.startOffset, offsetRange.endOffset),
            serializeFunction,
            executorService
          )
        )
      }
    }

    futures = Futures.inCompletionOrder(futures)
    // Build response content JSON
    val singlePathJsons: MutableList<String?> = ArrayList()

    for (future in futures) {
      val partialPathResult = future.get()
      if (!partialPathResult.isAllNulls) {
        singlePathJsons.add("\"" + partialPathResult.path + "\":" + partialPathResult.json)
      }
    }
    return "{\"from\":${queryRange.from},\"to\":${queryRange.to},\"step\":$rollup,\"series\":{${Joiner.on(",").skipNulls().join(singlePathJsons)}}}"
  }

  @Throws(ExecutionException::class, InterruptedException::class, TooMuchDataExpectedException::class)
  override fun getMetricsAsList(tenant: String, paths: List<Path>, from: Long, to: Long): List<TimeSeries> {
    // Calculate rollup etc
    val queryRange = QueryRange(from, to, rollup)
    val queryOffsetRanges = createQueryOffsetRanges(queryRange)
    logger.debug(queryRange.toString())
    logger.debug("Expected number of series is ${paths.size}")
    if (paths.size * queryRange.expectedCount > maxPoints) {
      logger.debug("Expected total number of data points exceeds the limit: ${paths.size * queryRange.expectedCount}")
      throw TooMuchDataExpectedException("Expected total number of data points exceeds the limit: ${paths.size * queryRange.expectedCount} (the limit is $maxPoints)")
    }
    // Now let's query C*
    var futures: MutableList<ListenableFuture<PartialPathResult>> = Lists.newArrayListWithExpectedSize(paths.size * queryOffsetRanges.keys.size)
    for (path in paths) {
      for ((startTime, offsetRange) in queryOffsetRanges) {
        val serializeFunction = Function<ResultSet, PartialPathResult> {
          resultSet: ResultSet? ->
          val result = PartialPathResult(path, startTime, offsetRange)
          result.makeArray(resultSet)
          result
        }
        futures.add(
          Futures.transform(
            executeAsync(path.path, tenant, startTime, offsetRange.startOffset, offsetRange.endOffset),
            serializeFunction,
            executorService
          )
        )
      }
    }

    val timer = Stopwatch.createStarted()
    futures = Futures.inCompletionOrder(futures)
    val timeSeriesList: MutableList<TimeSeries> = ArrayList()
    val timeSeriesMap = Maps.newTreeMap<String, TimeSeries>()
    for (future in futures) {
      val partialPathResult = future.get()
      if (partialPathResult.values != null) {
        if (! timeSeriesMap.containsKey(partialPathResult.path)) {
          timeSeriesMap[partialPathResult.path] = TimeSeries(partialPathResult.path, partialPathResult.path, partialPathResult.tags, queryRange)
        }
        timeSeriesMap[partialPathResult.path]!!.addValues(partialPathResult.values, partialPathResult.startTime)
      }
    }

    timeSeriesList.addAll(timeSeriesMap.values)
    logger.debug("Number of series fetched: " + timeSeriesList.size)
    timer.stop()
    logger.debug("Fetching from Cassandra took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds (" + paths + ")")
    var totalPoints = 0
    for (ts in timeSeriesList) {
      totalPoints += ts.values.size
    }

    return timeSeriesList
  }

  private fun createQueryOffsetRanges(queryRange: QueryRange): Map<Long, OffsetRange> {
    val from = queryRange.from
    val to = queryRange.to
    val rollup = queryRange.rollup
    var startTime = from - from % bucketSize
    val queryOffsetRange = Maps.newTreeMap<Long, OffsetRange>()
    while (startTime <= to) {
      val offsetRange = OffsetRange()
      if (startTime <= from) {
        offsetRange.startOffset = ((from - startTime) / rollup).toShort()
      } else {
        offsetRange.startOffset = 0
      }
      if (startTime + bucketSize < to) {
        offsetRange.endOffset = ((bucketSize / rollup) - 1).toShort()
      } else {
        offsetRange.endOffset = ((to - startTime) / rollup).toShort()
      }
      queryOffsetRange[startTime] = offsetRange
      startTime += bucketSize
    }
    return queryOffsetRange
  }

  override fun getRollup(): Int {
    return rollup
  }

  private class PartialPathResult constructor(path: Path, val startTime: Long, val offsetRange: OffsetRange) {
    val path: String = path.path
    val tags: Map<String, String> = path.getTags()
    var json: String? = null
    var values: Array<Double?>? = null
    var isAllNulls = true

    fun makeJson(resultSet: ResultSet?) {
      values = arrayOfNulls(offsetRange.endOffset - offsetRange.startOffset + 1)
      for (row in resultSet!!) {
        isAllNulls = false
        values!![row.getShort("offset").toInt() - offsetRange.startOffset] = row.getDouble("data")
      }
      json = Gson().toJson(values)
    }

    fun makeArray(resultSet: ResultSet?) {
      values = arrayOfNulls(offsetRange.endOffset - offsetRange.startOffset + 1)
      if (resultSet!!.availableWithoutFetching > 0) {
        isAllNulls = false
        for (row in resultSet) {
          values!![row.getShort("offset").toInt() - offsetRange.startOffset] = row.getDouble("data")
        }
      }
    }

    companion object {
      private fun isSumMetric(path: String): Boolean {
        return path.startsWith("sum")
      }
    }
  }

  companion object {
    val logger = LogManager.getLogger(OffsetBasedDataFetchHandler::class.java)
  }
}
