package com.graphene.writer

import com.graphene.writer.config.GrapheneWriterConfiguration
import com.graphene.writer.config.ElasticsearchKeyStoreConfiguration
import com.graphene.writer.config.StatsConfiguration
import com.graphene.writer.config.CassandraDataStoreConfiguration
import com.graphene.writer.config.CarbonConfiguration
import com.graphene.writer.blacklist.BlacklistConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.EnableMBeanExport
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableMBeanExport
@SpringBootApplication(exclude = [CassandraAutoConfiguration::class, GsonAutoConfiguration::class])
@EnableConfigurationProperties(
  BlacklistConfiguration::class,
  GrapheneWriterConfiguration::class,
  CarbonConfiguration::class,
  ElasticsearchKeyStoreConfiguration::class,
  StatsConfiguration::class,
  CassandraDataStoreConfiguration::class
)
class GrapheneWriterApplication

fun main(args: Array<String>) {
  SpringApplication.run(GrapheneWriterApplication::class.java, *args)
}
