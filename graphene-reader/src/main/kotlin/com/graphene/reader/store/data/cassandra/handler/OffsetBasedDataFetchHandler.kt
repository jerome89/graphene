package com.graphene.reader.store.data.cassandra.handler

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.PreparedStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.ResultSetFuture
import com.datastax.driver.core.Session
import com.google.common.base.Function
import com.google.common.base.Stopwatch
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.graphene.common.beans.OffsetRange
import com.graphene.common.beans.Path
import com.graphene.common.beans.SeriesRange
import com.graphene.common.store.data.cassandra.CassandraFactory
import com.graphene.reader.beans.TimeSeries
import com.graphene.reader.exceptions.TooMuchDataExpectedException
import com.graphene.reader.service.metric.DataFetchHandler
import com.graphene.reader.store.data.DataFetchHandlerProperty
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * @author Andrei Ivanov
 * @author dark
 * @author jerome89
 */
class OffsetBasedDataFetchHandler(
  cassandraFactory: CassandraFactory,
  dataFetchHandlerProperty: DataFetchHandlerProperty
) : DataFetchHandler {

  private val query: String
  private var rollup: Int = 60
  private var cluster: Cluster = cassandraFactory.createCluster(dataFetchHandlerProperty.property)
  private var session: Session
  private var statement: PreparedStatement
  private var maxPoints: Int = Int.MAX_VALUE
  private var bucketSize: Int = 604800

  init {
    this.rollup = dataFetchHandlerProperty.rollup
    this.query = """
    SELECT offset, data
        FROM ${dataFetchHandlerProperty.keyspace}_${dataFetchHandlerProperty.bucketSize}.${dataFetchHandlerProperty.columnFamily}_${rollup}s
        WHERE path = ?
              AND tenant = ?
              AND startTime = ?
              AND offset >= ?
              AND offset <= ?
        ORDER BY offset;"""
    this.session = cluster.connect()
    this.statement = session.prepare(query)
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
  override fun getMetrics(tenant: String, paths: List<Path>, from: Long, to: Long): List<TimeSeries> {
    // Calculate rollup etc
    val seriesRange = SeriesRange(from, to, rollup)
    val queryOffsetRanges = createQueryOffsetRanges(seriesRange)
    logger.debug(seriesRange.toString())
    logger.debug("Expected number of series is ${paths.size}")
    if (paths.size * seriesRange.expectedCount > maxPoints) {
      logger.debug("Expected total number of data points exceeds the limit: ${paths.size * seriesRange.expectedCount}")
      throw TooMuchDataExpectedException("Expected total number of data points exceeds the limit: ${paths.size * seriesRange.expectedCount} (the limit is $maxPoints)")
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
          timeSeriesMap[partialPathResult.path] = TimeSeries(partialPathResult.path, partialPathResult.path, partialPathResult.tags, seriesRange)
        }
        timeSeriesMap[partialPathResult.path]!!.addValues(partialPathResult.values, partialPathResult.startTime)
      }
    }

    timeSeriesList.addAll(timeSeriesMap.values)
    logger.debug("Number of series fetched: ${timeSeriesList.size}")
    timer.stop()
    logger.debug("Fetching from Cassandra took ${timer.elapsed(TimeUnit.MILLISECONDS)} milliseconds ($paths)")
    var totalPoints = 0
    for (ts in timeSeriesList) {
      totalPoints += ts.values.size
    }

    return timeSeriesList
  }

  fun createQueryOffsetRanges(seriesRange: SeriesRange): Map<Long, OffsetRange> {
    val from = seriesRange.from
    val to = seriesRange.to
    val rollup = seriesRange.rollup
    var startTime = from - from % (bucketSize * rollup)
    val queryOffsetRange = Maps.newTreeMap<Long, OffsetRange>()
    while (startTime <= to) {
      val offsetRange = OffsetRange()
      if (startTime <= from) {
        offsetRange.startOffset = ((from - startTime) / rollup).toShort()
      } else {
        offsetRange.startOffset = 0
      }
      if (startTime + (bucketSize * rollup) < to) {
        offsetRange.endOffset = (((bucketSize * rollup) / rollup) - 1).toShort()
      } else {
        offsetRange.endOffset = ((to - startTime) / rollup).toShort()
      }
      queryOffsetRange[startTime] = offsetRange
      startTime += (bucketSize * rollup)
    }
    return queryOffsetRange
  }

  override fun getRollup(): Int {
    return rollup
  }

  @PreDestroy
  fun shutdown() {
    logger.info("Shutting down OffsetBasedDataFetchHandler.")
    logger.info("Closing OffsetBasedDataFetchHandler C* session.")
    logger.info("Waiting for C* queries to be completed.")
    while (getInFlightQueries(session.state) > 0) {
      try {
        Thread.sleep(100)
      } catch (ignored: InterruptedException) {
      }
    }
    session.close()
    logger.info("Closing OffsetBasedDataFetchHandler.")
    cluster.close()
  }

  private fun getInFlightQueries(state: Session.State): Int {
    var result = 0
    val hosts = state.connectedHosts
    for (host in hosts) {
      result += state.getInFlightQueries(host)
    }
    return result
  }

  class PartialPathResult constructor(path: Path, val startTime: Long, private val offsetRange: OffsetRange) {
    val path: String = path.path
    val tags: Map<String, String> = path.getTags()
    var values: Array<Double?>? = null
    var isAllNulls = true

    fun makeArray(resultSet: ResultSet?) {
      values = arrayOfNulls(offsetRange.endOffset - offsetRange.startOffset + 1)
      if (resultSet!!.availableWithoutFetching > 0) {
        isAllNulls = false
        for (row in resultSet) {
          values!![row.getShort("offset").toInt() - offsetRange.startOffset] = row.getDouble("data")
        }
      }
    }
  }

  companion object {
    val logger: Logger = LogManager.getLogger(OffsetBasedDataFetchHandler::class.java)
  }
}
