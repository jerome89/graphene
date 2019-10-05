package com.graphene.writer.store.key

import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.store.StoreHandler
import org.apache.log4j.Logger
import org.elasticsearch.client.transport.TransportClient

import javax.annotation.PreDestroy
import java.util.concurrent.ConcurrentLinkedQueue
import javax.annotation.PostConstruct

/**
 * @author Andrei Ivanov
 * @author dark
 */
class ElasticsearchKeyStoreHandler(
  private val elasticsearchFactory: ElasticsearchFactory
) : StoreHandler {

  private val logger = Logger.getLogger(ElasticsearchKeyStoreHandler::class.java)

  private lateinit var client: TransportClient
  private lateinit var indexThread: IndexThread

  private val metrics = ConcurrentLinkedQueue<GrapheneMetric>()

  @PostConstruct
  fun init() {
    client = elasticsearchFactory.transportClient()

    indexThread = IndexThread(
      "grapheneIndexThread",
      client,
      metrics,
      elasticsearchFactory.elasticsearchKeyStoreConfiguration,
      elasticsearchFactory.bulkProcessor(client),
      GrapheneKeyMapper()
    )
    indexThread.start()
  }

  override fun handle(grapheneMetric: GrapheneMetric) {
    metrics.offer(grapheneMetric)
  }

  @PreDestroy
  fun shutdown() {
    indexThread.shutdown()
    logger.info("Sleeping for 10 seconds to allow leftovers to be written")
    try {
      Thread.sleep(10000)
    } catch (ignored: InterruptedException) {
    }

    logger.info("Closing ES client")
    client.close()
  }
}
