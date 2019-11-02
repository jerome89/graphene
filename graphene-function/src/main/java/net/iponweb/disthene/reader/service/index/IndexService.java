package net.iponweb.disthene.reader.service.index;

import net.iponweb.disthene.reader.exceptions.TooMuchDataExpectedException;
import com.graphene.common.HierarchyMetricPaths;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IndexService {

  Set<String> getPaths(String tenant, List<String> wildcards, Long from, Long to) throws TooMuchDataExpectedException;

  Collection<HierarchyMetricPaths.HierarchyMetricPath> getHierarchyMetricPaths(String tenant, String query, Long from, Long to) throws TooMuchDataExpectedException;

}
