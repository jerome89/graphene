package com.graphene.writer.store.data

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ConsistencyLevel
import com.datastax.driver.core.HostDistance
import com.datastax.driver.core.PoolingOptions
import com.datastax.driver.core.ProtocolOptions
import com.datastax.driver.core.ProtocolVersion
import com.datastax.driver.core.QueryOptions
import com.datastax.driver.core.SocketOptions
import org.apache.log4j.Logger

class CassandraFactory {

  private val logger = Logger.getLogger(CassandraFactory::class.java)

  fun createCluster(cassandraDataStoreHandlerProperty: CassandraDataStoreHandlerProperty): Cluster {
    var builder = Cluster.builder()
      .withSocketOptions(socketOptions(cassandraDataStoreHandlerProperty))
      .withCompression(ProtocolOptions.Compression.LZ4)
      .withLoadBalancingPolicy(CassandraLoadBalancingPolicy.createLoadBalancingPolicy(cassandraDataStoreHandlerProperty.loadBalancingPolicyName))
      .withPoolingOptions(poolingOptions(cassandraDataStoreHandlerProperty))
      .withQueryOptions(QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE))
      .withProtocolVersion(ProtocolVersion.valueOf(cassandraDataStoreHandlerProperty.protocolVersion))
      // TODO Enable jmx reporting
      .withoutJMXReporting()
      .withPort(cassandraDataStoreHandlerProperty.port)

    if (cassandraDataStoreHandlerProperty.userName != null && cassandraDataStoreHandlerProperty.userPassword != null) {
      builder = builder.withCredentials(cassandraDataStoreHandlerProperty.userName, cassandraDataStoreHandlerProperty.userPassword)
    }

    for (cp in cassandraDataStoreHandlerProperty.cluster) {
      builder.addContactPoint(cp)
    }

    val cluster = builder.build()
    val metadata = cluster.metadata
    logger.debug("Connected to cluster: " + metadata.clusterName)
    for (host in metadata.allHosts) {
      logger.debug(String.format("Datacenter: %s; Host: %s; Rack: %s", host.datacenter, host.address, host.rack))
    }
    return cluster
  }

  private fun poolingOptions(cassandraDataStoreHandlerProperty: CassandraDataStoreHandlerProperty): PoolingOptions {
    val poolingOptions = PoolingOptions()
    poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, cassandraDataStoreHandlerProperty.maxConnections)
    poolingOptions.setMaxConnectionsPerHost(HostDistance.REMOTE, cassandraDataStoreHandlerProperty.maxConnections)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.REMOTE, cassandraDataStoreHandlerProperty.maxRequests)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, cassandraDataStoreHandlerProperty.maxRequests)
    return poolingOptions
  }

  private fun socketOptions(cassandraDataStoreHandlerProperty: CassandraDataStoreHandlerProperty): SocketOptions? {
    return SocketOptions()
      .setReceiveBufferSize(1024 * 1024)
      .setSendBufferSize(1024 * 1024)
      .setTcpNoDelay(false)
      .setReadTimeoutMillis(cassandraDataStoreHandlerProperty.readTimeout * 1000)
      .setConnectTimeoutMillis(cassandraDataStoreHandlerProperty.connectTimeout * 1000)
  }
}
