package com.graphene.writer.store.data

import com.datastax.driver.core.*
import com.graphene.writer.config.CassandraDataStoreConfiguration
import org.apache.log4j.Logger

class CassandraFactory {

  private val logger = Logger.getLogger(CassandraFactory::class.java)

  fun createCluster(cassandraDataStoreConfiguration: CassandraDataStoreConfiguration): Cluster {
    var builder = Cluster.builder()
      .withSocketOptions(socketOptions(cassandraDataStoreConfiguration))
      .withCompression(ProtocolOptions.Compression.LZ4)
      .withLoadBalancingPolicy(CassandraLoadBalancingPolicies.getLoadBalancingPolicy(cassandraDataStoreConfiguration.loadBalancingPolicyName))
      .withPoolingOptions(poolingOptions(cassandraDataStoreConfiguration))
      .withQueryOptions(QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE))
      .withProtocolVersion(ProtocolVersion.valueOf(cassandraDataStoreConfiguration.protocolVersion))
      // TODO Enable jmx reporting
      .withoutJMXReporting()
      .withPort(cassandraDataStoreConfiguration.port)

    if (cassandraDataStoreConfiguration.userName != null && cassandraDataStoreConfiguration.userPassword != null) {
      builder = builder.withCredentials(cassandraDataStoreConfiguration.userName, cassandraDataStoreConfiguration.userPassword)
    }

    for (cp in cassandraDataStoreConfiguration.cluster) {
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

  private fun poolingOptions(cassandraDataStoreConfiguration: CassandraDataStoreConfiguration): PoolingOptions {
    val poolingOptions = PoolingOptions()
    poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, cassandraDataStoreConfiguration.maxConnections)
    poolingOptions.setMaxConnectionsPerHost(HostDistance.REMOTE, cassandraDataStoreConfiguration.maxConnections)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.REMOTE, cassandraDataStoreConfiguration.maxRequests)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, cassandraDataStoreConfiguration.maxRequests)
    return poolingOptions
  }

  private fun socketOptions(cassandraDataStoreConfiguration: CassandraDataStoreConfiguration): SocketOptions? {
    return SocketOptions()
      .setReceiveBufferSize(1024 * 1024)
      .setSendBufferSize(1024 * 1024)
      .setTcpNoDelay(false)
      .setReadTimeoutMillis(cassandraDataStoreConfiguration.readTimeout * 1000)
      .setConnectTimeoutMillis(cassandraDataStoreConfiguration.connectTimeout * 1000)
  }

}