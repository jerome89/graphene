package net.iponweb.disthene.service.blacklist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrei Ivanov
 */
@ConfigurationProperties(prefix = "graphene.writer")
public class BlacklistConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BlacklistConfiguration.class);

    private Map<String, List<String>> blacklist = new HashMap<>();

    @PostConstruct
    public void init() {
        logger.info("Load Graphene blacklist configuration : {}", toString());
    }

    public Map<String, List<String>> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Map<String, List<String>> blacklist) {
        this.blacklist = blacklist;
    }

    @Override
    public String toString() {
        return "BlackListConfiguration{" +
                "rules=" + blacklist +
                '}';
    }
}
