package net.iponweb.disthene.util;

import com.datastax.driver.core.policies.*;

public class CassandraLoadBalancingPolicies {

    public static final String tokenAwarePolicy = "TokenAwarePolicy";
    public static final String tokenDcAwareRoundRobinPolicy = "TokenDcAwareRoundRobinPolicy";
    public static final String tokenLatencyAwarePolicy = "TokenLatencyAwarePolicy";

    public static LoadBalancingPolicy getLoadBalancingPolicy(String policy) {
        switch (policy) {
            case tokenAwarePolicy:
                return new TokenAwarePolicy(new RoundRobinPolicy());
            case tokenDcAwareRoundRobinPolicy:
                return new TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().build());
            case tokenLatencyAwarePolicy:
                return new TokenAwarePolicy(LatencyAwarePolicy.builder(new RoundRobinPolicy()).build());
            default:
                throw new IllegalArgumentException("Cassandra load balancing policy can be " + tokenAwarePolicy + " ," + tokenLatencyAwarePolicy
                       + " ," + tokenDcAwareRoundRobinPolicy);
        }
    }
}
