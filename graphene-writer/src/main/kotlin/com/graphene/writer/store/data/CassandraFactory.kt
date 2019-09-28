package com.graphene.writer.store.data

import com.datastax.driver.core.*
import com.graphene.writer.config.StoreConfiguration
import net.iponweb.disthene.util.CassandraLoadBalancingPolicies
import org.apache.log4j.Logger

class CassandraFactory {

  private val logger = Logger.getLogger(CassandraFactory::class.java)

  fun createCluster(storeConfiguration: StoreConfiguration): Cluster {
    var builder = Cluster.builder()
      .withSocketOptions(socketOptions(storeConfiguration))
      .withCompression(ProtocolOptions.Compression.LZ4)
      .withLoadBalancingPolicy(CassandraLoadBalancingPolicies.getLoadBalancingPolicy(storeConfiguration.loadBalancingPolicyName))
      .withPoolingOptions(poolingOptions(storeConfiguration))
      .withQueryOptions(QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE))
      .withProtocolVersion(ProtocolVersion.valueOf(storeConfiguration.protocolVersion))
      // TODO Enable jmx reporting
      .withoutJMXReporting()
      .withPort(storeConfiguration.port)

    if (storeConfiguration.userName != null && storeConfiguration.userPassword != null) {
      builder = builder.withCredentials(storeConfiguration.userName, storeConfiguration.userPassword)
    }

    for (cp in storeConfiguration.cluster) {
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

  private fun poolingOptions(storeConfiguration: StoreConfiguration): PoolingOptions {
    val poolingOptions = PoolingOptions()
    poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, storeConfiguration.maxConnections)
    poolingOptions.setMaxConnectionsPerHost(HostDistance.REMOTE, storeConfiguration.maxConnections)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.REMOTE, storeConfiguration.maxRequests)
    poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, storeConfiguration.maxRequests)
    return poolingOptions
  }

  private fun socketOptions(storeConfiguration: StoreConfiguration): SocketOptions? {
    return SocketOptions()
      .setReceiveBufferSize(1024 * 1024)
      .setSendBufferSize(1024 * 1024)
      .setTcpNoDelay(false)
      .setReadTimeoutMillis(storeConfiguration.readTimeout * 1000)
      .setConnectTimeoutMillis(storeConfiguration.connectTimeout * 1000)
  }

}