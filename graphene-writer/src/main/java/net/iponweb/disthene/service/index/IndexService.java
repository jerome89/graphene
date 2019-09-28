package net.iponweb.disthene.service.index;

import com.graphene.writer.input.GrapheneMetric;
import com.graphene.writer.config.IndexConfiguration;
import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Andrei Ivanov
 */
@Component
public class IndexService {

    private Logger logger = Logger.getLogger(IndexService.class);

    private TransportClient client;
    private IndexThread indexThread;

    private Queue<GrapheneMetric> metrics = new ConcurrentLinkedQueue<>();

    public IndexService(IndexConfiguration indexConfiguration) {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", indexConfiguration.getName())
                .build();
        client = new TransportClient(settings);
        for (String node : indexConfiguration.getCluster()) {
            client.addTransportAddress(new InetSocketTransportAddress(node, indexConfiguration.getPort()));
        }

        indexThread = new IndexThread(
                "grapheneIndexThread",
                client,
                metrics,
                indexConfiguration.getIndex(),
                indexConfiguration.getType(),
                indexConfiguration.getBulk().getActions(),
                indexConfiguration.getBulk().getInterval()
        );

        indexThread.start();
    }

    public void handle(GrapheneMetric grapheneMetric) {
        metrics.offer(grapheneMetric);
    }

    @PreDestroy
    public void shutdown() {
        indexThread.shutdown();
        logger.info("Sleeping for 10 seconds to allow leftovers to be written");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ignored) {
        }
        logger.info("Closing ES client");
        client.close();
    }
}
