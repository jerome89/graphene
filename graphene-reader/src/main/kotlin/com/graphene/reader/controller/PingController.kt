package com.graphene.reader.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {

  @GetMapping("/ping")
  fun getPing(x: Any): String {
    return "ok"
  }
}
