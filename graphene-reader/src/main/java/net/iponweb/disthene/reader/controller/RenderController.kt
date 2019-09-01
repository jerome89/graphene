package net.iponweb.disthene.reader.controller

import net.iponweb.disthene.reader.handler.RenderHandler
import net.iponweb.disthene.reader.handler.parameters.RenderParameters
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RenderController(
  private val renderHandler: RenderHandler
) {

  @PostMapping("/render")
  fun postRender(renderParameters: RenderParameters): String {
    val fullHttpResponse = renderHandler.handle(renderParameters)
    return fullHttpResponse.content().toString()
  }

}