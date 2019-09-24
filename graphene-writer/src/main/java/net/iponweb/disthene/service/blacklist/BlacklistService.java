package net.iponweb.disthene.service.blacklist;

import com.google.common.base.Joiner;
import com.graphene.writer.input.GrapheneMetric;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andrei Ivanov
 */
@Component
public class BlacklistService {

    private Map<String, Pattern> rules = new HashMap<>();

    public BlacklistService(BlacklistConfiguration blackListConfiguration) {
        for(Map.Entry<String, List<String>> entry : blackListConfiguration.getBlacklist().entrySet()) {
            rules.put(entry.getKey(), Pattern.compile(Joiner.on("|").skipNulls().join(entry.getValue())));
        }
    }

    public boolean isBlackListed(GrapheneMetric grapheneMetric) {
        Pattern pattern = rules.get(grapheneMetric.getTenant());
        if (pattern != null) {
            Matcher matcher = pattern.matcher(grapheneMetric.getGraphiteKey());
            return matcher.matches();
        } else {
            return false;
        }
    }

    public void setRules(BlacklistConfiguration blackListConfiguration) {
        Map<String, Pattern> rules = new HashMap<>();

        for(Map.Entry<String, List<String>> entry : blackListConfiguration.getBlacklist().entrySet()) {
            rules.put(entry.getKey(), Pattern.compile(Joiner.on("|").skipNulls().join(entry.getValue())));
        }

        this.rules = rules;
    }
}
