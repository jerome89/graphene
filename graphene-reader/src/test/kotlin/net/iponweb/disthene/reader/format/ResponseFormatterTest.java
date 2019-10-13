package net.iponweb.disthene.reader.format;

import com.graphene.reader.handler.RenderParameter;
import io.netty.handler.codec.http.FullHttpResponse;
import net.iponweb.disthene.reader.exceptions.LogarithmicScaleNotAllowed;
import org.assertj.core.util.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ResponseFormatterTest {

  @Test
  void shouldReturnEmptyArrayIfNotExistsTimeSeriesData() throws LogarithmicScaleNotAllowed {
    // when
    FullHttpResponse response =
        ResponseFormatter.formatResponse(Collections.emptyList(), defaultRenderParameter());

    // then
    assertThat("[]", is(new String(response.content().array(), StandardCharsets.UTF_8)));
  }

  @NotNull
  private RenderParameter defaultRenderParameter() {
    return new RenderParameter("NONE", Lists.newArrayList(), null, null, null, null, 480);
  }
}
