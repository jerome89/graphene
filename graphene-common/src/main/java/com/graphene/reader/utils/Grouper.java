package com.graphene.reader.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.EvaluationException;
import com.graphene.reader.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Grouper {

    interface AggregationMethod {
        Double apply(List<Double> points);
    }

    private static ImmutableMap<String, AggregationMethod> aggregationMap;
    static {
        AggregationMethod avg = CollectionUtils::average;
        AggregationMethod sum = CollectionUtils::sum;
        AggregationMethod min = CollectionUtils::min;
        AggregationMethod max = CollectionUtils::max;
        AggregationMethod median = CollectionUtils::median;
        AggregationMethod div = CollectionUtils::div;
        AggregationMethod count = points -> (double) points.size();

        aggregationMap = ImmutableMap.<String, AggregationMethod>builder()
            .put("sum", sum)
            .put("sumSeries", sum)
            .put("avg", avg)
            .put("average", avg)
            .put("min", min)
            .put("minSeries", min)
            .put("max", max)
            .put("maxSeries", max)
            .put("med", median)
            .put("median", median)
            .put("div", div)
            .put("divide", div)
            .put("count", count)
        .build();
    }

    private List<TimeSeries> timeSeries;
    private String aggregator;
    private Long from;
    private Long to;

    public Grouper(List<TimeSeries> ts, String aggregatorName, Long from, Long to) {
      this.timeSeries = ts;
      this.aggregator = aggregatorName;
      this.from = from;
      this.to = to;
    }

    public List<TimeSeries> byTagKeys(List<String> groupTagKeys) throws EvaluationException {
      Map<String, List<TimeSeries>> buckets = Maps.newHashMap();

      for (TimeSeries ts : timeSeries) {
        List<String> bucketNameParts = Lists.newArrayList();
        for (String groupTagKey : groupTagKeys) {
          for (Map.Entry<String, String> entry : ts.getTags().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(groupTagKey)) {
              bucketNameParts.add(entry.getValue());
            }
          }
        }
        String bucketName = Joiner.on(".").join(bucketNameParts);
        if (buckets.containsKey(bucketName)) {
          buckets.get(bucketName).add(ts);
        } else {
          buckets.put(bucketName, Lists.newArrayList(ts));
        }
      }

      return reduceFromBucket(buckets);
    }

    public List<TimeSeries> byNodesIndex(int[] indexes) throws EvaluationException {
        Map<String, List<TimeSeries>> buckets = new HashMap<>();

        for (TimeSeries ts : timeSeries) {
            String bucketName = getBucketName(ts.getName(), indexes);
            if (!buckets.containsKey(bucketName)) buckets.put(bucketName, new ArrayList<>());
            buckets.get(bucketName).add(ts);
        }
        return reduceFromBucket(buckets);
    }

    public static boolean hasAggregationMethod(String name) {
        return aggregationMap.containsKey(name);
    }

    public static String[] getAvailableAggregationMethods() {
        return aggregationMap.keySet().toArray(new String[aggregationMap.size()]);
    }

    private String getBucketName(String name, int[] positions) {
        String[] split = name.split("\\.");
        List<String> parts = new ArrayList<>();
        for (int position : positions) {
            parts.add(split[position]);
        }
        return Joiner.on(".").skipNulls().join(parts);
    }

    private List<TimeSeries> reduceFromBucket(Map<String, List<TimeSeries>> buckets) throws EvaluationException {
      long from = this.from;
      long to = this.to;
      int step = timeSeries.get(0).getStep();
      int length = timeSeries.get(0).getValues().length;

      List<TimeSeries> resultTimeSeries = new ArrayList<>();
      try {
        for (Map.Entry<String, List<TimeSeries>> bucket : buckets.entrySet()) {
          TimeSeries timeSeries = new TimeSeries(bucket.getKey(), from, to, step);
          Double[] values = new Double[length];

          for (int i = 0; i < length; i++) {
            List<Double> points = new ArrayList<>();
            for (TimeSeries ts : bucket.getValue()) {
              points.add(ts.getValues()[i]);
            }
            values[i] = aggregationMap.get(aggregator).apply(points);
          }
          timeSeries.setValues(values);
          timeSeries.setName(bucket.getKey());
          timeSeries.setPathExpression(bucket.getKey());
          timeSeries.setTags(bucket.getValue().get(0).getTags());
          resultTimeSeries.add(timeSeries);
        }
        return resultTimeSeries;
      } catch (Exception e) {
        throw new EvaluationException("Cannot reduce series with aggregator = " + aggregator);
      }
    }
}
