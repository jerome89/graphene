package net.iponweb.disthene.reader.controller

import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpRequest
import net.iponweb.disthene.reader.handler.PathsHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PathsController(
  private val pathsHandler: PathsHandler
) {

  @RequestMapping("/paths")
  fun paths(request: HttpRequest): FullHttpResponse {
    return pathsHandler.handle(request)
  }
}
