package com.graphene.writer.config

import java.util.concurrent.Executor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class GrapheneWriterConfig {

  @Bean
  fun grapheneProcessorExecutor(): Executor {
    val executor = ThreadPoolTaskExecutor()
    executor.corePoolSize = Runtime.getRuntime().availableProcessors() * 2
    executor.maxPoolSize = Runtime.getRuntime().availableProcessors() * 2
    executor.setQueueCapacity(5000)
    executor.threadNamePrefix = "GrapheneProcessorExecutor-"
    executor.setWaitForTasksToCompleteOnShutdown(true)
    executor.setAwaitTerminationSeconds(30)
    executor.initialize()
    return executor
  }
}
