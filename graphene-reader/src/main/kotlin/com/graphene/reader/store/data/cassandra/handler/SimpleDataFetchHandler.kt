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
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.google.gson.Gson
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
class SimpleDataFetchHandler(
  cassandraFactory: CassandraFactory,
  dataFetchHandlerProperty: DataFetchHandlerProperty
) : DataFetchHandler {

  private val query: String = """
    SELECT time, data
    FROM ${dataFetchHandlerProperty.keyspace}.${dataFetchHandlerProperty.columnFamily}
    WHERE path = ?
          AND tenant = ?
          AND time >= ?
          AND time <= ?
    ORDER BY time;"""

  private var cluster: Cluster
  private var session: Session
  private var statement: PreparedStatement
  private var rollup: Int = 60
  private var maxPoints: Int = Int.MAX_VALUE

  init {
    val property = Jsons.from(dataFetchHandlerProperty.handler["property"], CassandraDataHandlerProperty::class.java)
    this.cluster = cassandraFactory.createCluster(property)
    this.session = cluster.connect()
    this.statement = session.prepare(query)
    this.rollup = dataFetchHandlerProperty.rollup
    this.maxPoints = dataFetchHandlerProperty.maxPoints
  }

  private fun executeAsync(path: String, tenant: String, from: Long, to: Long): ResultSetFuture {
    return session.executeAsync(
      statement.bind(
        path,
        tenant,
        from,
        to
      )
    )
  }

  private val executorService: ExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool())
  @Throws(ExecutionException::class, InterruptedException::class, TooMuchDataExpectedException::class)
  override fun getMetricsAsJson(tenant: String, paths: List<Path>, from: Long, to: Long): String { // Calculate rollup etc
    val queryRange = QueryRange(from, to, rollup)
    logger.debug(queryRange.toString())
    logger.debug("Expected number of series is: ${paths.size}")
    // now build the weird data structures ("in the meanwhile")
    val timestampIndices: MutableMap<Long, Int> = HashMap()
    var timestamp = queryRange.from
    var index = 0
    while (timestamp <= queryRange.to) {
      timestampIndices[timestamp] = index++
      timestamp += rollup.toLong()
    }
    val length = timestampIndices.size
    // Fail (return empty list) right away if we exceed maximum number of points
    if (paths.size * length > maxPoints) {
      logger.debug("Expected total number of data points exceeds the limit: " + paths.size * length)
      throw TooMuchDataExpectedException("Expected total number of data points exceeds the limit: " + paths.size * length + " (the limit is " + maxPoints + ")")
    }
    // Now let's query C*
    var futures: MutableList<ListenableFuture<SinglePathResult>> = Lists.newArrayListWithExpectedSize(paths.size)
    for (path in paths) {
      val serializeFunction = Function<ResultSet, SinglePathResult> { resultSet: ResultSet? ->
        val result = SinglePathResult(path)
        result.makeJson(resultSet, length, timestampIndices)
        result
      }
      futures.add(
        Futures.transform(
          executeAsync(path.path, tenant, queryRange.from, queryRange.to),
          serializeFunction,
          executorService
        )
      )
    }
    futures = Futures.inCompletionOrder(futures)
    // Build response content JSON
    val singlePathJsons: MutableList<String?> = ArrayList()
    for (future in futures) {
      val singlePathResult = future.get()
      if (!singlePathResult.isAllNulls) {
        singlePathJsons.add("\"" + singlePathResult.path + "\":" + singlePathResult.json)
      }
    }
    return "{\"from\":${queryRange.from},\"to\":${queryRange.to},\"step\":$rollup,\"series\":{${Joiner.on(",").skipNulls().join(singlePathJsons)}}}"
  }

  @Throws(ExecutionException::class, InterruptedException::class, TooMuchDataExpectedException::class)
  override fun getMetricsAsList(tenant: String, paths: List<Path>, from: Long, to: Long): List<TimeSeries> {
    val queryRange = QueryRange(from, to, rollup)
    logger.debug(queryRange.toString())
    logger.debug("Expected number of series is: ${paths.size}")
    // now build the weird data structures ("in the meanwhile")
    val timestampIndices: MutableMap<Long, Int> = HashMap()
    var timestamp = queryRange.from
    var index = 0
    while (timestamp <= queryRange.to) {
      timestampIndices[timestamp] = index++
      timestamp += rollup.toLong()
    }
    val length = timestampIndices.size
    // Fail (return empty list) right away if we exceed maximum number of points
    if (paths.size * length > maxPoints) {
      logger.debug("Expected total number of data points exceeds the limit: " + paths.size * length)
      throw TooMuchDataExpectedException("Expected total number of data points exceeds the limit: " + paths.size * length + " (the limit is " + maxPoints + ")")
    }
    // Now let's query C*
    var futures: MutableList<ListenableFuture<SinglePathResult>> = Lists.newArrayListWithExpectedSize(paths.size)
    for (path in paths) {
      val serializeFunction = Function<ResultSet, SinglePathResult> { resultSet: ResultSet? ->
        val result = SinglePathResult(path)
        result.makeArray(resultSet, length, timestampIndices)
        result
      }
      futures.add(
        Futures.transform(
          executeAsync(path.path, tenant, queryRange.from, queryRange.to),
          serializeFunction,
          executorService
        )
      )
    }
    val timer = Stopwatch.createStarted()
    futures = Futures.inCompletionOrder(futures)
    val timeSeries: MutableList<TimeSeries> = ArrayList()
    for (future in futures) {
      val singlePathResult = future.get()
      if (singlePathResult.values != null) {
        val ts = TimeSeries(singlePathResult.path, queryRange.from, queryRange.to, rollup)
        ts.values = singlePathResult.values
        ts.tags = singlePathResult.tags
        timeSeries.add(ts)
      }
    }
    logger.debug("Number of series fetched: " + timeSeries.size)
    timer.stop()
    logger.debug("Fetching from Cassandra took " + timer.elapsed(TimeUnit.MILLISECONDS) + " milliseconds (" + paths + ")")
    // sort it by path
    timeSeries.sortWith(Comparator.comparing { obj: TimeSeries -> obj.name })
    var totalPoints = 0
    for (ts in timeSeries) {
      totalPoints += ts.values.size
    }
    return timeSeries
  }

  override fun getRollup(): Int {
    return rollup
  }

  private class SinglePathResult constructor(path: Path) {
    var path: String
    var json: String? = null
    var tags: Map<String, String>
    var values: Array<Double?>? = null
    var isAllNulls = true

    fun makeJson(resultSet: ResultSet?, length: Int, timestampIndices: Map<Long, Int>) {
      val values = arrayOfNulls<Double>(length)
      for (row in resultSet!!) {
        isAllNulls = false
        values[timestampIndices[row.getLong("time")]!!] = row.getDouble("data")
      }
      json = Gson().toJson(values)
    }

    fun makeArray(resultSet: ResultSet?, length: Int, timestampIndices: Map<Long, Int>) {
      if (resultSet!!.availableWithoutFetching > 0) {
        isAllNulls = false
        values = arrayOfNulls(length)
        for (row in resultSet) {
          values!![timestampIndices[row.getLong("time")]!!] = row.getDouble("data")
        }
      } else {
        values = arrayOfNulls(length)
        for ((_, value) in timestampIndices) {
          values!![value] = null
        }
      }
    }

    companion object {
      private fun isSumMetric(path: String): Boolean {
        return path.startsWith("sum")
      }
    }

    init {
      this.path = path.path
      tags = path.getTags()
    }
  }

  companion object {
    val logger = LogManager.getLogger(SimpleDataFetchHandler::class.java)
  }
}
