package net.iponweb.disthene.reader;

import com.graphene.reader.store.IndexProperty;
import com.graphene.reader.store.key.selector.KeySelectorProperty;
import net.iponweb.disthene.reader.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.graphene.reader", "net.iponweb.disthene.reader"})
@EnableAutoConfiguration(exclude = {CassandraAutoConfiguration.class, GsonAutoConfiguration.class})
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
