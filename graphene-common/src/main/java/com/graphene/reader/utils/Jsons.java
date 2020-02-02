package com.graphene.reader.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphene.reader.exceptions.ParsingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Jsons {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private Jsons() {}

  public static ObjectMapper mapper() {
    return MAPPER;
  }

  public static String stringify(Object value) {
    try {
      return MAPPER.writeValueAsString(value);
    } catch (Exception e) {
      throw new ParsingException(e);
    }
  }

  public static <T> T from(Object value, Class<T> valueType) {
    try {
      return MAPPER.readValue(stringify(value), valueType);
    } catch (IOException e) {
      throw new ParsingException(e);
    }
  }

  public static Map<String, Object> from(Object value) {
    try {
      return MAPPER.readValue(stringify(value), new TypeReference<HashMap<String,Object>>() {});
    } catch (IOException e) {
      throw new ParsingException(e);
    }
  }
}
