package com.graphene.writer.store.data

import com.datastax.driver.core.*
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import com.graphene.writer.config.CassandraDataStoreConfiguration
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.StoreHandler
import net.iponweb.disthene.service.aggregate.CarbonConfiguration
import org.apache.log4j.Logger

import javax.annotation.PreDestroy
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.annotation.PostConstruct

/**
 * @author Andrei Ivanov
 * @author dark
 *
 * @since 1.0.0
 */
class CassandraDataStoreHandler(
  carbonConfiguration: CarbonConfiguration,
  private val cassandraDataStoreConfiguration: CassandraDataStoreConfiguration,
  private val cassandraFactory: CassandraFactory
) : StoreHandler {

  private val logger = Logger.getLogger(CassandraDataStoreHandler::class.java)
  private val rollup: Int = carbonConfiguration.baseRollup.rollup
  private val period: Int = carbonConfiguration.baseRollup.period
  private val query: String = """
    UPDATE ${cassandraDataStoreConfiguration.keyspace}.${cassandraDataStoreConfiguration.columnFamily} 
    USING TTL ? 
    SET data = ? 
    WHERE tenant = ? 
          AND rollup = ? 
          AND period = ? 
          AND path = ? 
          AND time = ?;"""

  private lateinit var cluster: Cluster
  private lateinit var session: Session
  private lateinit var statement: PreparedStatement
  private lateinit var executor: Executor

  @PostConstruct
  fun init() {
    this.cluster = cassandraFactory.createCluster(cassandraDataStoreConfiguration)
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
      rollup * period,
      listOf(grapheneMetric.value),
      grapheneMetric.getTenant(),
      rollup,
      period,
      grapheneMetric.getGraphiteKey(),
      grapheneMetric.timestamp)
  }

  @PreDestroy
  fun shutdown() {
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
