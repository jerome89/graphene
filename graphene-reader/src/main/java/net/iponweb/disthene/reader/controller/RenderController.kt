package net.iponweb.disthene.reader.controller

import net.iponweb.disthene.reader.handler.RenderHandler
import net.iponweb.disthene.reader.handler.parameters.RenderParameters
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets

@RestController
class RenderController(
  private val renderHandler: RenderHandler
) {

  @PostMapping("/render")
  fun postRender(
    @ModelAttribute("renderRequest") renderRequest: RenderRequest
  ): String {
    val response = renderHandler.handle(RenderParameters.from(renderRequest))
    return String(response.content().array(), StandardCharsets.UTF_8)
  }

  data class RenderRequest(
      val target: String,
      val from: String?,
      val until: String?,
      val format: String?,
      val maxDataPoints: Int?
  )
}
