package net.iponweb.disthene.reader;

import net.iponweb.disthene.reader.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {CassandraAutoConfiguration.class, GsonAutoConfiguration.class})
@EnableConfigurationProperties({
  IndexConfiguration.class,
  StoreConfiguration.class,
  DistheneReaderConfiguration.class,
  ThrottlingConfiguration.class
})
public class GrapheneReaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrapheneReaderApplication.class, args);
    }
}
