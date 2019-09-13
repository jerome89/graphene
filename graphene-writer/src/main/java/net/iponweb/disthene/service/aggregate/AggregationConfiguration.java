package net.iponweb.disthene.service.aggregate;

import com.google.common.collect.Lists;
import net.iponweb.disthene.bean.AggregationRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author Andrei Ivanov */
@ConfigurationProperties(prefix = "graphene.writer")
public class AggregationConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(AggregationConfiguration.class);

  private Map<String, List<AggregationRule>> aggregationRules = new HashMap<>();
  private Map<String, Map<String, String>> aggregations = new HashMap<>();

  @PostConstruct
  public void init() {
    for (Map.Entry<String, Map<String, String>> aggregation : aggregations.entrySet()) {

      if (!aggregationRules.containsKey(aggregation.getKey())) {
        aggregationRules.put(aggregation.getKey(), Lists.newArrayList());
      }

      List<AggregationRule> aggregationRuleList = aggregationRules.get(aggregation.getKey());

      for (Map.Entry<String, String> aggregationRuleRaw : aggregation.getValue().entrySet()) {
        aggregationRuleList.add(
            new AggregationRule(aggregationRuleRaw.getKey(), aggregationRuleRaw.getValue()));
      }

      this.aggregationRules.put(aggregation.getKey(), aggregationRuleList);
    }

    logger.info("Load Graphene aggregation configuration : {}", toString());
  }

  public Map<String, List<AggregationRule>> getAggregationRules() {
    return aggregationRules;
  }

  public void setAggregations(Map<String, Map<String, String>> aggregations) {
    this.aggregations = aggregations;
  }

  @Override
  public String toString() {
    return "AggregationConfiguration{" + "aggregationRules=" + aggregationRules + '}';
  }
}
