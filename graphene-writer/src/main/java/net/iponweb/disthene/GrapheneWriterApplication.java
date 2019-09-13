package net.iponweb.disthene;

import net.iponweb.disthene.config.IndexConfiguration;
import net.iponweb.disthene.config.StatsConfiguration;
import net.iponweb.disthene.config.StoreConfiguration;
import net.iponweb.disthene.service.aggregate.AggregationConfiguration;
import net.iponweb.disthene.service.aggregate.CarbonConfiguration;
import net.iponweb.disthene.config.DistheneConfiguration;
import net.iponweb.disthene.service.blacklist.BlacklistConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {CassandraAutoConfiguration.class, GsonAutoConfiguration.class})
@EnableConfigurationProperties({BlacklistConfiguration.class, DistheneConfiguration.class, CarbonConfiguration.class, AggregationConfiguration.class, IndexConfiguration.class, StatsConfiguration.class, StoreConfiguration.class})
public class GrapheneWriterApplication {
  public static void main(String[] args) {
    SpringApplication.run(GrapheneWriterApplication.class, args);
  }
}
