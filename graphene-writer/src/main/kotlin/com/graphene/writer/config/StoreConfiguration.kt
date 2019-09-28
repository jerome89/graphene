package com.graphene.writer.config

import net.iponweb.disthene.util.CassandraLoadBalancingPolicies
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

import javax.annotation.PostConstruct
import java.util.ArrayList

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer.store")
class StoreConfiguration {

  var cluster: List<String> = ArrayList()
  var keyspace: String? = null
  var columnFamily: String? = null
  var userName: String? = null
  var userPassword: String? = null
  var port: Int = 0
  var maxConnections: Int = 0
  var readTimeout: Int = 0
  var connectTimeout: Int = 0
  var maxRequests: Int = 0
  var isBatch: Boolean = false
  var batchSize: Int = 0
  var pool: Int = 0
  var loadBalancingPolicyName = CassandraLoadBalancingPolicies.tokenDcAwareRoundRobinPolicy
  var protocolVersion = "V2"

  @PostConstruct
  fun init() {
    logger.info("Load Graphene store configuration : {}", toString())
  }

  override fun toString(): String {
    return "StoreConfiguration{" +
      "cluster=" + cluster +
      ", keyspace='" + keyspace + '\''.toString() +
      ", columnFamily='" + columnFamily + '\''.toString() +
      ", userName='" + userName + '\''.toString() +
      ", userPassword='" + userPassword + '\''.toString() +
      ", port=" + port +
      ", maxConnections=" + maxConnections +
      ", readTimeout=" + readTimeout +
      ", connectTimeout=" + connectTimeout +
      ", maxRequests=" + maxRequests +
      ", batch=" + isBatch +
      ", batchSize=" + batchSize +
      ", pool=" + pool +
      ", loadBalancingPolicyName='" + loadBalancingPolicyName + '\''.toString() +
      ", protocolVersion='" + protocolVersion + '\''.toString() +
      '}'.toString()
  }

  companion object {

    private val logger = LoggerFactory.getLogger(StoreConfiguration::class.java)
  }
}
