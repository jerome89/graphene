package net.iponweb.disthene.config

import net.engio.mbassy.bus.MBassador
import net.engio.mbassy.bus.config.BusConfiguration
import net.engio.mbassy.bus.config.Feature
import net.iponweb.disthene.events.DistheneEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class GrapheneWriterConfig {

  @Bean
  open fun bus(): MBassador<DistheneEvent> {
    return MBassador(BusConfiguration()
      .addFeature(Feature.SyncPubSub.Default())
      .addFeature(Feature.AsynchronousHandlerInvocation.Default())
      .addFeature(Feature.AsynchronousMessageDispatch.Default())
    )
  }

}