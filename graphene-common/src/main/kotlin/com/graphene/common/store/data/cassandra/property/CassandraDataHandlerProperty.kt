package com.graphene.common.store.data.cassandra.property

import com.datastax.driver.core.ConsistencyLevel
import com.datastax.driver.core.ProtocolVersion
import com.graphene.common.store.data.cassandra.CassandraLoadBalancingPolicy
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory

/**
 * @author Andrei Ivanov
 * @author dark
 */
class CassandraDataHandlerProperty(
  var cluster: List<String> = arrayListOf(),
  var port: Int = 0,
  var userName: String? = null,
  var userPassword: String? = null,
  var maxConnections: Int = 0,
  var readTimeout: Int = 0,
  var connectTimeout: Int = 0,
  var maxRequests: Int = 0,
  var maxQueueSize: Int = 4 * 1024 * 1024,
  var loadBalancingPolicyName: String = CassandraLoadBalancingPolicy.tokenDcAwareRoundRobinPolicy,
  var consistencyLevel: ConsistencyLevel = ConsistencyLevel.ONE,
  var protocolVersion: ProtocolVersion = ProtocolVersion.V4
) {

  @PostConstruct
  fun init() {
    logger.info("Load Graphene cassandraDataHandlerProperty configuration : {}", toString())
  }

  override fun toString(): String {
    return "CassandraDataHandlerProperty{" +
      "cluster=$cluster" +
      ", port=$port" +
      ", userName=$userName" +
      ", userPassword=$userPassword" +
      ", maxConnections=$maxConnections" +
      ", readTimeout=$readTimeout" +
      ", connectTimeout=$connectTimeout" +
      ", maxRequests=$maxRequests" +
      ", maxQueueSize=$maxQueueSize" +
      ", loadBalancingPolicyName=$loadBalancingPolicyName" +
      ", consistencyLevel=$consistencyLevel" +
      ", protocolVersion=$protocolVersion}"
  }

  companion object {
    private val logger = LoggerFactory.getLogger(CassandraDataHandlerProperty::class.java)
  }
}
