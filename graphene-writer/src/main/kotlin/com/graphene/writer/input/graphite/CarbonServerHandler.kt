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
import org.springframework.stereotype.Component

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
  private lateinit var graphiteCodec: GraphiteMetricConverter

  @PostConstruct
  fun init() {
    this.rollup = carbonProperty.baseRollup!!
    this.graphiteCodec = GraphiteMetricConverter()
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
        grapheneProcessor.process(graphiteCodec.convert(GraphiteMetric(metric.path, metric.value, normalizeTimestamp(metric.timestamp))))
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
}
