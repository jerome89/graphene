package com.graphene.reader.service.index;

import com.graphene.common.beans.Path;
import com.graphene.reader.exceptions.TooMuchDataExpectedException;
import com.graphene.common.HierarchyMetricPaths;

import java.util.Collection;
import java.util.List;

public interface KeySearchHandler {

  Collection<Path> getPaths(String tenant, List<String> pathExpressions, Long from, Long to) throws TooMuchDataExpectedException;

  Collection<Path> getPathsByTags(String tenant, List<String> tagExpressions, Long from, Long to) throws TooMuchDataExpectedException;

  Collection<HierarchyMetricPaths.HierarchyMetricPath> getHierarchyMetricPaths(String tenant, String pathExpression, Long from, Long to) throws TooMuchDataExpectedException;

}
