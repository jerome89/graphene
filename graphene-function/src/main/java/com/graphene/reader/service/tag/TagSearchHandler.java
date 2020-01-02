package com.graphene.reader.service.tag;

import java.util.List;

public interface TagSearchHandler {
  List<String> getTags(String tagPrefix, List<String> tagExpressions, String tag, Long from, Long to, int limit);

  List<String> getTagValues(String valuePrefix, List<String> tagExpressions, String tag, Long from, Long to, int limit);
}
