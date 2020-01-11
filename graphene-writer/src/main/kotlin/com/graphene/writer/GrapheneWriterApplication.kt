package com.graphene.writer

import com.graphene.writer.blacklist.BlacklistConfiguration
import com.graphene.writer.config.GrapheneWriterProperty
import com.graphene.writer.config.StatsProperty
import com.graphene.writer.input.graphite.property.InputGraphiteCarbonProperty
import com.graphene.writer.input.graphite.property.InputGraphiteProperty
import com.graphene.writer.input.kafka.config.InputKafkaProperty
import com.graphene.writer.store.data.CassandraDataStoreHandlerProperty
import com.graphene.writer.store.data.StoreDataProperty
import com.graphene.writer.store.key.KeyStoreHandlersProperty
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
  GrapheneWriterProperty::class,
  InputGraphiteCarbonProperty::class,
  StatsProperty::class,
  CassandraDataStoreHandlerProperty::class,
  InputGraphiteProperty::class,
  StoreDataProperty::class,
  KeyStoreHandlersProperty::class,
  InputKafkaProperty::class
)
class GrapheneWriterApplication

fun main(args: Array<String>) {
  SpringApplication.run(GrapheneWriterApplication::class.java, *args)
}
