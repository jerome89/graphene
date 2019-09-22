package com.graphene.writer

import net.engio.mbassy.bus.MBassador
import net.engio.mbassy.bus.config.BusConfiguration
import net.engio.mbassy.bus.config.Feature
import net.iponweb.disthene.events.DistheneEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import javax.annotation.PreDestroy

@Configuration
class GrapheneWriterConfig {

  @Bean
  fun bus(): MBassador<DistheneEvent> {
    return MBassador(BusConfiguration()
      .addFeature(Feature.SyncPubSub.Default())
      .addFeature(Feature.AsynchronousHandlerInvocation.Default())
      .addFeature(Feature.AsynchronousMessageDispatch.Default())
    )
  }

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
