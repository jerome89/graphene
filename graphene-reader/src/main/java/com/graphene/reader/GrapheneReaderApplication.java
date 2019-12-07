package com.graphene.reader;

import com.graphene.reader.config.GrapheneReaderProperties;
import com.graphene.reader.config.ReaderConfiguration;
import com.graphene.reader.config.StoreConfiguration;
import com.graphene.reader.config.ThrottlingConfiguration;
import com.graphene.reader.store.IndexProperty;
import com.graphene.reader.store.key.selector.KeySelectorProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(
    scanBasePackages = {"com.graphene.reader"},
    exclude = {CassandraAutoConfiguration.class, GsonAutoConfiguration.class})
@EnableConfigurationProperties({
  IndexProperty.class,
  StoreConfiguration.class,
  GrapheneReaderProperties.class,
  ThrottlingConfiguration.class,
  ReaderConfiguration.class,
  KeySelectorProperty.class
})
public class GrapheneReaderApplication {
  public static void main(String[] args) {
    SpringApplication.run(GrapheneReaderApplication.class, args);
  }
}
