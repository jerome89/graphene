package net.iponweb.disthene;

import com.graphene.writer.config.GrapheneWriterConfiguration;
import com.graphene.writer.config.ElasticsearchKeyStoreConfiguration;
import com.graphene.writer.config.StatsConfiguration;
import com.graphene.writer.config.CassandraDataStoreConfiguration;
import net.iponweb.disthene.service.aggregate.CarbonConfiguration;
import com.graphene.writer.blacklist.BlacklistConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableMBeanExport
@SpringBootApplication(scanBasePackages = {"com.graphene.writer", "net.iponweb.disthene"})
@EnableAutoConfiguration(exclude = {CassandraAutoConfiguration.class, GsonAutoConfiguration.class})
@EnableConfigurationProperties({BlacklistConfiguration.class, GrapheneWriterConfiguration.class, CarbonConfiguration.class, ElasticsearchKeyStoreConfiguration.class, StatsConfiguration.class, CassandraDataStoreConfiguration.class})
public class GrapheneWriterApplication {
  public static void main(String[] args) {
    SpringApplication.run(GrapheneWriterApplication.class, args);
  }
}
