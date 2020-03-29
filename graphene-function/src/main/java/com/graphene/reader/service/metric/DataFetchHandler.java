package com.graphene.reader.service.metric;

import com.graphene.common.beans.Path;
import com.graphene.reader.beans.TimeSeries;
import com.graphene.reader.exceptions.TooMuchDataExpectedException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DataFetchHandler {

  List<TimeSeries> getMetrics(String tenant, List<Path> paths, long from, long to) throws ExecutionException, InterruptedException, TooMuchDataExpectedException;

  int getRollup();
}
