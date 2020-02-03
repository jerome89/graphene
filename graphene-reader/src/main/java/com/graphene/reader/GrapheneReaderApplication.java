package com.graphene.reader;

import com.graphene.reader.config.RenderConfiguration;
import com.graphene.reader.config.ThrottlingConfiguration;
import com.graphene.reader.store.data.DataFetchHandlersProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(
    scanBasePackages = {"com.graphene.reader", "com.graphene.common.store"},
    exclude = {CassandraAutoConfiguration.class, GsonAutoConfiguration.class})
@EnableConfigurationProperties({
  DataFetchHandlersProperty.class,
  ThrottlingConfiguration.class,
  RenderConfiguration.class
})
public class GrapheneReaderApplication {
  public static void main(String[] args) {
    SpringApplication.run(GrapheneReaderApplication.class, args);
  }
}
