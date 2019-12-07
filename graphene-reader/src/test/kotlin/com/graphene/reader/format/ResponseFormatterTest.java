package com.graphene.reader.format;

import com.graphene.reader.handler.RenderParameter;
import com.graphene.reader.exceptions.LogarithmicScaleNotAllowed;
import org.assertj.core.util.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ResponseFormatterTest {

  @Test
  void shouldReturnEmptyArrayIfNotExistsTimeSeriesData() throws LogarithmicScaleNotAllowed {
    // when
    ResponseEntity<?> response = ResponseFormatter.formatResponse(Collections.emptyList(), defaultRenderParameter());

    // then
    assertThat("[]", is(response.getBody().toString()));
  }

  @NotNull
  private RenderParameter defaultRenderParameter() {
    return new RenderParameter("NONE", Lists.newArrayList(), null, null, null, null, 480);
  }
}
