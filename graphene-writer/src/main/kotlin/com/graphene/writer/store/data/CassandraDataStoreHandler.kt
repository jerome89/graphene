package com.graphene.writer.store.data

import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.PreparedStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.graphite.property.InputGraphiteCarbonProperty
import com.graphene.writer.store.DataStoreHandler
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.annotation.PostConstruct
import org.apache.logging.log4j.LogManager

/**
 * @author Andrei Ivanov
 * @author dark
 *
 * @since 1.0.0
 */
class CassandraDataStoreHandler(
  inputGraphiteCarbonProperty: InputGraphiteCarbonProperty,
  private val handlerProperty: CassandraDataStoreHandlerProperty,
  private val cassandraFactory: CassandraFactory
) : DataStoreHandler {

  private val logger = LogManager.getLogger(CassandraDataStoreHandler::class.java)
  private val retention: Int = inputGraphiteCarbonProperty.baseRollup!!.retention
  private val query: String = """
    UPDATE ${handlerProperty.keyspace}.${handlerProperty.columnFamily}
    USING TTL ?
    SET data = ?
    WHERE tenant = ?
          AND path = ?
          AND time = ?;"""

  private lateinit var cluster: Cluster
  private lateinit var session: Session
  private lateinit var statement: PreparedStatement
  private lateinit var executor: Executor

  @PostConstruct
  fun init() {
    this.cluster = cassandraFactory.createCluster(handlerProperty)
    this.session = cluster.connect()
    this.statement = session.prepare(query)
    this.executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool())
  }

  override fun handle(grapheneMetric: GrapheneMetric) {
    val future = session.executeAsync(boundStatement(grapheneMetric))

    Futures.addCallback(
      future,
      object : FutureCallback<ResultSet> {
        override fun onSuccess(result: ResultSet?) {
          // nothing
        }

        override fun onFailure(t: Throwable) {
          logger.error(t)
        }
      },
      executor)
  }

  private fun boundStatement(grapheneMetric: GrapheneMetric): BoundStatement {
    return statement.bind(
      retention,
      grapheneMetric.value,
      grapheneMetric.getTenant(),
      grapheneMetric.id,
      grapheneMetric.timestampSeconds)
  }

  override fun close() {
    doShutdown()
  }

  private fun doShutdown() {
    logger.info("Closing Cassandra session")
    logger.info("Waiting for Cassandra queries to be completed")
    waitToBeCompleted()
    session.close()
    logger.info("Closing Cassandra cluster")
    cluster.close()
    logger.info("Closed Cassandra cluster")
  }

  private fun waitToBeCompleted() {
    while (getInFlightQueries(session.state) > 0) {
      try {
        Thread.sleep(100)
      } catch (ignored: InterruptedException) {
      }
    }
  }

  private fun getInFlightQueries(state: Session.State): Int {
    var result = 0
    val hosts = state.connectedHosts
    for (host in hosts) {
      result += state.getInFlightQueries(host)
    }

    return result
  }
}
