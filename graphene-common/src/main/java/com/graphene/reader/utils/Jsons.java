package com.graphene.reader.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphene.reader.exceptions.ParsingException;
import com.graphene.reader.exceptions.ParsingException;

import java.io.IOException;

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
}
