package net.iponweb.disthene.reader

import net.iponweb.disthene.reader.config.IndexConfiguration
import org.apache.log4j.BasicConfigurator
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAutoConfiguration(exclude = [
    CassandraAutoConfiguration::class,
    GsonAutoConfiguration::class
])
@EnableConfigurationProperties(IndexConfiguration::class)
open class GrapheneReaderApplication

fun main(args: Array<String>) {
    runApplication<GrapheneReaderApplication>(*args)

    BasicConfigurator.configure()
}
