package net.iponweb.disthene.service.store;

import com.datastax.driver.core.*;
import com.google.common.util.concurrent.MoreExecutors;
import com.graphene.writer.input.GrapheneMetric;
import com.graphene.writer.storage.CassandraFactory;
import net.iponweb.disthene.config.StoreConfiguration;
import net.iponweb.disthene.service.aggregate.CarbonConfiguration;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

/**
 * @author Andrei Ivanov
 */
@Component
public class CassandraService {
    private Logger logger = Logger.getLogger(CassandraService.class);

    private Cluster cluster;
    private Session session;

    private Queue<GrapheneMetric> metrics = new ConcurrentLinkedQueue<>();
    private List<WriterThread> writerThreads = new ArrayList<>();

    public CassandraService(CarbonConfiguration carbonConfiguration, StoreConfiguration storeConfiguration, CassandraFactory cassandraFactory) {
        cluster = cassandraFactory.createCluster(storeConfiguration);

        session = cluster.connect();

        String query = "UPDATE " +
          storeConfiguration.getKeyspace() + "." + storeConfiguration.getColumnFamily() +
          " USING TTL ? SET data = data + ? WHERE tenant = ? AND rollup = ? AND period = ? AND path = ? AND time = ?;";

        PreparedStatement statement = session.prepare(query);

        // Creating writers
        createThread(carbonConfiguration, storeConfiguration, statement);
    }

    private void createThread(CarbonConfiguration carbonConfiguration, StoreConfiguration storeConfiguration, PreparedStatement statement) {
        if (storeConfiguration.isBatch()) {
            for (int i = 0; i < storeConfiguration.getPool(); i++) {
                WriterThread writerThread = new BatchWriterThread(
                        "grapheneCassandraBatchWriter" + i,
                        session,
                        statement,
                        metrics,
                        MoreExecutors.listeningDecorator(Executors.newCachedThreadPool()),
                        storeConfiguration,
                        carbonConfiguration
                );

                writerThreads.add(writerThread);
                writerThread.start();
            }
        } else {
            for (int i = 0; i < storeConfiguration.getPool(); i++) {
                WriterThread writerThread = new SingleWriterThread(
                        "grapheneCassandraSingleWriter" + i,
                        session,
                        statement,
                        metrics,
                        MoreExecutors.listeningDecorator(Executors.newCachedThreadPool()),
                  storeConfiguration,
                        carbonConfiguration
                );

                writerThreads.add(writerThread);
                writerThread.start();
            }
        }
    }

    public void handle(GrapheneMetric grapheneMetric) {
        metrics.offer(grapheneMetric);
    }

    @PreDestroy
    public void shutdown() {
        for (WriterThread writerThread : writerThreads) {
            writerThread.shutdown();
        }

        logger.info("Closing C* session");
        logger.info("Waiting for C* queries to be completed");
        while (getInFlightQueries(session.getState()) > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
        session.close();
        logger.info("Closing C* cluster");
        cluster.close();
    }

    private int getInFlightQueries(Session.State state) {
        int result = 0;
        Collection<Host> hosts = state.getConnectedHosts();
        for(Host host : hosts) {
            result += state.getInFlightQueries(host);
        }

        return result;
    }
}
