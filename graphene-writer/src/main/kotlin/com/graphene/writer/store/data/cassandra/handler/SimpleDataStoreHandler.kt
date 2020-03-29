package com.graphene.writer.store.data.cassandra.handler

import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.PreparedStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import com.graphene.common.store.data.cassandra.CassandraFactory
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.DataStoreHandler
import com.graphene.writer.store.DataStoreHandlerProperty
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import org.apache.logging.log4j.LogManager

/**
 * @author Andrei Ivanov
 * @author dark
 * @author jerome89
 *
 * @since 1.0.0
 */
class SimpleDataStoreHandler(
  cassandraFactory: CassandraFactory,
  dataStoreHandlerProperty: DataStoreHandlerProperty
) : DataStoreHandler {

  private val logger = LogManager.getLogger(SimpleDataStoreHandler::class.java)
  private val ttl = dataStoreHandlerProperty.ttl
  private val query: String = """
    UPDATE ${dataStoreHandlerProperty.keyspace}.${dataStoreHandlerProperty.columnFamily}
    USING TTL ?
    SET data = ?
    WHERE tenant = ?
          AND path = ?
          AND time = ?;"""

  private var cluster: Cluster = cassandraFactory.createCluster(dataStoreHandlerProperty.property)
  private var session: Session
  private var statement: PreparedStatement
  private var executor: Executor

  init {
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
      ttl,
      grapheneMetric.value,
      grapheneMetric.getTenant(),
      grapheneMetric.id,
      grapheneMetric.timestampSeconds
    )
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
