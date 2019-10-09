package com.graphene.writer.store.data

import com.datastax.driver.core.*
import org.apache.log4j.Logger

class CassandraFactory {

  private val logger = Logger.getLogger(CassandraFactory::class.java)

  fun createCluster(cassandraDataStoreProperties: CassandraDataStoreProperties): Cluster {
    var builder = Cluster.builder()
      .withSocketOptions(socketOptions(cassandraDataStoreProperties))
      .withCompression(ProtocolOptions.Compression.LZ4)
      .withLoadBalancingPolicy(CassandraLoadBalancingPolicy.createLoadBalancingPolicy(cassandraDataStoreProperties.loadBalancingPolicyName))
      .withPoolingOptions(poolingOptions(cassandraDataStoreProperties))
      .withQueryOptions(QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE))
      .withProtocolVersion(ProtocolVersion.valueOf(cassandraDataStoreProperties.protocolVersion))
      // TODO Enable jmx reporting
      .withoutJMXReporting()
      .withPort(cassandraDataStoreProperties.port)

    if (cassandraDataStoreProperties.userName != null && cassandraDataStoreProperties.userPassword != null) {
      builder = builder.withCredentials(cassandraDataStoreProperties.userName, cassandraDataStoreProperties.userPassword)
    }

    for (cp in cassandraDataStoreProperties.cluster) {
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

  private fun poolingOptions(cassandraDataStoreProperties: CassandraDataStoreProperties): PoolingOptions {
    val poolingOptions = PoolingOptions()
    poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, cassandraDataStoreProperties.maxConnections)
    poolingOptions.setMaxConnectionsPerHost(HostDistance.REMOTE, cassandraDataStoreProperties.maxConnections)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.REMOTE, cassandraDataStoreProperties.maxRequests)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, cassandraDataStoreProperties.maxRequests)
    return poolingOptions
  }

  private fun socketOptions(cassandraDataStoreProperties: CassandraDataStoreProperties): SocketOptions? {
    return SocketOptions()
      .setReceiveBufferSize(1024 * 1024)
      .setSendBufferSize(1024 * 1024)
      .setTcpNoDelay(false)
      .setReadTimeoutMillis(cassandraDataStoreProperties.readTimeout * 1000)
      .setConnectTimeoutMillis(cassandraDataStoreProperties.connectTimeout * 1000)
  }

}