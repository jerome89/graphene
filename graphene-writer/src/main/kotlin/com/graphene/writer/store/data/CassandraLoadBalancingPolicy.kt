package com.graphene.writer.store.data

import com.datastax.driver.core.policies.*

class CassandraLoadBalancingPolicy {

  companion object {
    internal const val tokenAwarePolicy = "TokenAwarePolicy"
    internal const val tokenDcAwareRoundRobinPolicy = "TokenDcAwareRoundRobinPolicy"
    internal const val tokenLatencyAwarePolicy = "TokenLatencyAwarePolicy"

    fun createLoadBalancingPolicy(policy: String): LoadBalancingPolicy {
      return when (policy) {
        tokenAwarePolicy -> TokenAwarePolicy(RoundRobinPolicy())
        tokenDcAwareRoundRobinPolicy -> TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().build())
        tokenLatencyAwarePolicy -> TokenAwarePolicy(LatencyAwarePolicy.builder(RoundRobinPolicy()).build())
        else -> throw IllegalArgumentException("Cassandra load balancing policy can be $tokenAwarePolicy , " +
          "$tokenLatencyAwarePolicy , $tokenDcAwareRoundRobinPolicy")
      }
    }
  }
}
