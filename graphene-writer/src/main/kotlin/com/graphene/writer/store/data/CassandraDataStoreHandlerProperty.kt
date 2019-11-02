package com.graphene.writer.store.data

import java.util.ArrayList
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Andrei Ivanov
 * @author dark
 */
@ConfigurationProperties(prefix = "graphene.writer.store.data.handlers.cassandra")
class CassandraDataStoreHandlerProperty {

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
  var loadBalancingPolicyName = CassandraLoadBalancingPolicy.tokenDcAwareRoundRobinPolicy
  var protocolVersion = "V2"

  @PostConstruct
  fun init() {
    logger.info("Load Graphene cassandraDataStoreHandlerProperty configuration : {}", toString())
  }

  override fun toString(): String {
    return "CassandraDataStoreHandlerProperty{" +
      "cluster=$cluster" +
      ", keyspace=$keyspace" +
      ", columnFamily=$columnFamily" +
      ", userName=$userName" +
      ", userPassword=$userPassword" +
      ", port=$port" +
      ", maxConnections=$maxConnections" +
      ", readTimeout=$readTimeout" +
      ", connectTimeout=$connectTimeout" +
      ", maxRequests=$maxRequests" +
      ", batch=$isBatch" +
      ", batchSize=$batchSize" +
      ", pool=$pool" +
      ", loadBalancingPolicyName=$loadBalancingPolicyName" +
      ", protocolVersion=$protocolVersion}"
  }

  companion object {

    private val logger = LoggerFactory.getLogger(CassandraDataStoreHandlerProperty::class.java)
  }
}
