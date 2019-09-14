package net.iponweb.disthene.config;

import net.iponweb.disthene.service.aggregate.AggregationConfiguration;
import net.iponweb.disthene.util.CassandraLoadBalancingPolicies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer.store")
public class StoreConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(StoreConfiguration.class);

    private List<String> cluster = new ArrayList<>();
    private String keyspace;
    private String columnFamily;
    private String userName;
    private String userPassword;
    private int port;
    private int maxConnections;
    private int readTimeout;
    private int connectTimeout;
    private int maxRequests;
    private boolean batch;
    private int batchSize;
    private int pool;
    private String loadBalancingPolicyName = CassandraLoadBalancingPolicies.tokenDcAwareRoundRobinPolicy;
    private String protocolVersion = "V2";

    @PostConstruct
    public void init() {
        logger.info("Load Graphene store configuration : {}", toString());
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public List<String> getCluster() {
        return cluster;
    }

    public void setCluster(List<String> cluster) {
        this.cluster = cluster;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getPool() {
        return pool;
    }

    public void setPool(int pool) {
        this.pool = pool;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public void setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
    }

    public String getLoadBalancingPolicyName() {
        return loadBalancingPolicyName;
    }

    public void setLoadBalancingPolicyName(String policy) {
        this.loadBalancingPolicyName = policy;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    public String toString() {
        return "StoreConfiguration{" +
                "cluster=" + cluster +
                ", keyspace='" + keyspace + '\'' +
                ", columnFamily='" + columnFamily + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", port=" + port +
                ", maxConnections=" + maxConnections +
                ", readTimeout=" + readTimeout +
                ", connectTimeout=" + connectTimeout +
                ", maxRequests=" + maxRequests +
                ", batch=" + batch +
                ", batchSize=" + batchSize +
                ", pool=" + pool +
                ", loadBalancingPolicyName='" + loadBalancingPolicyName + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                '}';
    }
}
