package com.graphene.common.store.data.cassandra

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ConsistencyLevel
import com.datastax.driver.core.HostDistance
import com.datastax.driver.core.PoolingOptions
import com.datastax.driver.core.ProtocolOptions
import com.datastax.driver.core.ProtocolVersion
import com.datastax.driver.core.QueryOptions
import com.datastax.driver.core.SocketOptions
import com.graphene.common.store.data.cassandra.property.CassandraDataHandlerProperty
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

@Component
class CassandraFactory {

  private val logger = LogManager.getLogger(CassandraFactory::class.java)

  fun createCluster(cassandraDataHandlerProperty: CassandraDataHandlerProperty): Cluster {
    var builder = Cluster.builder()
      .withSocketOptions(socketOptions(cassandraDataHandlerProperty))
      .withCompression(ProtocolOptions.Compression.LZ4)
      .withLoadBalancingPolicy(CassandraLoadBalancingPolicy.createLoadBalancingPolicy(cassandraDataHandlerProperty.loadBalancingPolicyName))
      .withPoolingOptions(poolingOptions(cassandraDataHandlerProperty))
      .withQueryOptions(QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM))
      .withProtocolVersion(ProtocolVersion.valueOf(cassandraDataHandlerProperty.protocolVersion))
      // TODO Enable jmx reporting
      .withoutJMXReporting()
      .withPort(cassandraDataHandlerProperty.port)

    if (cassandraDataHandlerProperty.userName != null && cassandraDataHandlerProperty.userPassword != null) {
      builder = builder.withCredentials(cassandraDataHandlerProperty.userName, cassandraDataHandlerProperty.userPassword)
    }

    builder.addContactPoint(cassandraDataHandlerProperty.cluster)
//    for (cp in cassandraDataStoreHandlerProperty.cluster) {
//      builder.addContactPoint(cp)
//    }

    val cluster = builder.build()
    val metadata = cluster.metadata
    logger.debug("Connected to cluster: " + metadata.clusterName)
    for (host in metadata.allHosts) {
      logger.debug(String.format("Datacenter: %s; Host: %s; Rack: %s", host.datacenter, host.address, host.rack))
    }
    return cluster
  }

  private fun poolingOptions(cassandraDataHandlerProperty: CassandraDataHandlerProperty): PoolingOptions {
    val poolingOptions = PoolingOptions()
    poolingOptions.maxQueueSize = cassandraDataHandlerProperty.maxQueueSize
    poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, cassandraDataHandlerProperty.maxConnections)
    poolingOptions.setMaxConnectionsPerHost(HostDistance.REMOTE, cassandraDataHandlerProperty.maxConnections)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.REMOTE, cassandraDataHandlerProperty.maxRequests)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, cassandraDataHandlerProperty.maxRequests)
    return poolingOptions
  }

  private fun socketOptions(cassandraDataHandlerProperty: CassandraDataHandlerProperty): SocketOptions? {
    return SocketOptions()
      .setReceiveBufferSize(1024 * 1024)
      .setSendBufferSize(1024 * 1024)
      .setTcpNoDelay(false)
      .setReadTimeoutMillis(cassandraDataHandlerProperty.readTimeout * 1000)
      .setConnectTimeoutMillis(cassandraDataHandlerProperty.connectTimeout * 1000)
  }
}
