package net.iponweb.disthene.reader.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.context.annotation.Bean

@Configuration
open class GrapheneReaderConfiguration {

  @Bean
  open fun corsConfigurer(): WebMvcConfigurer {
    return object : WebMvcConfigurer {
      override fun addCorsMappings(registry: CorsRegistry?) {
        registry!!.addMapping("/**")
      }
    }
  }
}
