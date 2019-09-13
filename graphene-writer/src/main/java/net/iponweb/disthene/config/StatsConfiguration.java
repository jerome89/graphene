package net.iponweb.disthene.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer.stats")
public class StatsConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(StatsConfiguration.class);

    private int interval;
    private String tenant;
    private String hostname;
    private boolean log;

    @PostConstruct
    public void init() {
        logger.info("Load Graphene stats configuration : {}", toString());
    }

    public StatsConfiguration() {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "unknown";
        }
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "StatsConfiguration{" +
                "interval=" + interval +
                ", tenant='" + tenant + '\'' +
                ", hostname='" + hostname + '\'' +
                ", log=" + log +
                '}';
    }
}
