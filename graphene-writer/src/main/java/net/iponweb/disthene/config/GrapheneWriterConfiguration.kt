package net.iponweb.disthene.config

import net.iponweb.disthene.service.aggregate.AggregationConfiguration
import net.iponweb.disthene.service.blacklist.BlacklistConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "graphene.writer")
class GrapheneWriterConfiguration(
  val blacklistConfiguration: BlacklistConfiguration,
  val aggregationConfiguration: AggregationConfiguration
)