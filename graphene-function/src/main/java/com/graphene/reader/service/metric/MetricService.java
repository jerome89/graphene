package com.graphene.reader.service.metric;

import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.config.Rollup;
import com.graphene.reader.exceptions.TooMuchDataExpectedException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface MetricService {

  String getMetricsAsJson(String tenant, List<String> wildcards, long from, long to) throws ExecutionException, InterruptedException, TooMuchDataExpectedException;

  List<TimeSeries> getMetricsAsList(String tenant, Set<String> paths, long from, long to) throws ExecutionException, InterruptedException, TooMuchDataExpectedException;

  Rollup getRollup(long from);
}
