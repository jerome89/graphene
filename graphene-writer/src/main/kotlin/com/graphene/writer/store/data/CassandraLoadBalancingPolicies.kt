package com.graphene.writer.store.data

import com.datastax.driver.core.policies.*

object CassandraLoadBalancingPolicies {

  val tokenAwarePolicy = "TokenAwarePolicy"
  val tokenDcAwareRoundRobinPolicy = "TokenDcAwareRoundRobinPolicy"
  val tokenLatencyAwarePolicy = "TokenLatencyAwarePolicy"

  fun getLoadBalancingPolicy(policy: String): LoadBalancingPolicy {
    when (policy) {
      tokenAwarePolicy -> return TokenAwarePolicy(RoundRobinPolicy())
      tokenDcAwareRoundRobinPolicy -> return TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().build())
      tokenLatencyAwarePolicy -> return TokenAwarePolicy(LatencyAwarePolicy.builder(RoundRobinPolicy()).build())
      else -> throw IllegalArgumentException("Cassandra load balancing policy can be " + tokenAwarePolicy + " ," + tokenLatencyAwarePolicy
        + " ," + tokenDcAwareRoundRobinPolicy)
    }
  }
}
