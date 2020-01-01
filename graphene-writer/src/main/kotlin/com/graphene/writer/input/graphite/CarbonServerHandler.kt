package com.graphene.writer.input.graphite

import com.google.common.base.CharMatcher
import com.graphene.writer.config.Rollup
import com.graphene.writer.domain.Metric
import com.graphene.writer.input.graphite.property.CarbonProperty
import com.graphene.writer.processor.GrapheneProcessor
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.CharsetUtil
import javax.annotation.PostConstruct
import org.apache.logging.log4j.LogManager
import org.jmxtrans.embedded.QueryResult
import org.jmxtrans.embedded.output.GraphiteWriter
import org.springframework.stereotype.Component
import java.util.Objects
import javax.annotation.PreDestroy

/**
 * @author Andrei Ivanov
 */
@Component
@ChannelHandler.Sharable
class CarbonServerHandler(
  private val carbonProperty: CarbonProperty,
  private val grapheneProcessor: GrapheneProcessor
) : ChannelInboundHandlerAdapter() {

  private val logger = LogManager.getLogger(CarbonServerHandler::class.java)

  private lateinit var rollup: Rollup
  private lateinit var graphiteConverter: GraphiteMetricConverter
  private lateinit var graphiteWriter: GraphiteWriter

  @PostConstruct
  fun init() {
    this.rollup = carbonProperty.baseRollup!!
    this.graphiteConverter = GraphiteMetricConverter()
    if (Objects.nonNull(carbonProperty.route)) {
      this.graphiteWriter = GraphiteWriter()
      this.graphiteWriter.settings["host"] = carbonProperty.route!!.host
      this.graphiteWriter.settings["port"] = carbonProperty.route!!.port
      this.graphiteWriter.start()
    }
  }

  @Throws(Exception::class)
  override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
    val byteBuf = msg as ByteBuf

    try {
      val metric = Metric(byteBuf.toString(CharsetUtil.UTF_8).trim { it <= ' ' }, rollup)
      if (System.currentTimeMillis() / 1000L - metric.timestamp > 3600) {
        logger.warn("Metric is from distant past (older than 1 hour): $metric")
      }

      if (CharMatcher.ascii().matchesAllOf(metric.path) && CharMatcher.ascii().matchesAllOf(metric.tenant)) {
        val grapheneMetrics = graphiteConverter.convert(GraphiteMetric(metric.path, metric.value, normalizeTimestamp(metric.timestamp)))
        for (grapheneMetric in grapheneMetrics) {
          grapheneProcessor.process(grapheneMetric)

          if (Objects.nonNull(carbonProperty.route)) {
            graphiteWriter.write(listOf(QueryResult(metric.path, metric.value, metric.timestamp * 1_000L)))
          }
        }
      } else {
        logger.warn("Non ASCII characters received, discarding: $metric")
      }
    } catch (e: Exception) {
      logger.error(e)
    }

    byteBuf.release()
  }

  fun normalizeTimestamp(timestamp: Long): Long {
    return timestamp / rollup.rollup * rollup.rollup
  }

  @PreDestroy
  fun destroy() {
    graphiteWriter.stop()
  }
}
