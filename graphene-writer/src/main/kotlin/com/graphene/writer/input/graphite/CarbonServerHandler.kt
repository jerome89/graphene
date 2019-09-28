package com.graphene.writer.input.graphite

import com.google.common.base.CharMatcher
import com.graphene.writer.processor.GrapheneProcessor
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.CharsetUtil
import com.graphene.writer.domain.Metric
import com.graphene.writer.config.GrapheneWriterConfiguration
import com.graphene.writer.config.Rollup
import org.apache.log4j.Logger
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * @author Andrei Ivanov
 */
@Component
@ChannelHandler.Sharable
class CarbonServerHandler(
        private val configuration: GrapheneWriterConfiguration,
        private val grapheneProcessor: GrapheneProcessor
) : ChannelInboundHandlerAdapter() {

  private val logger = Logger.getLogger(CarbonServerHandler::class.java)

  private lateinit var rollup: Rollup
  private lateinit var graphiteCodec: GraphiteCodec

  @PostConstruct
  fun init() {
    this.rollup = configuration.carbon.baseRollup!!
    this.graphiteCodec = GraphiteCodec()
  }

  @Throws(Exception::class)
  override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
    val byteBuf = msg as ByteBuf

    try {
      val metric = Metric(byteBuf.toString(CharsetUtil.UTF_8).trim { it <= ' ' }, rollup)
      if (System.currentTimeMillis() / 1000L - metric.timestamp > 3600) {
        logger.warn("Metric is from distant past (older than 1 hour): $metric")
      }

      if (CharMatcher.ASCII.matchesAllOf(metric.path) && CharMatcher.ASCII.matchesAllOf(metric.tenant)) {
        grapheneProcessor.process(graphiteCodec.encode(GraphiteMetric(metric.path, metric.value, normalizeTimestamp(metric.timestamp))))
      } else {
        logger.warn("Non ASCII characters received, discarding: $metric")
      }
    } catch (e: Exception) {
      logger.trace(e)
    }

    byteBuf.release()
  }

  fun normalizeTimestamp(timestamp: Long): Long {
    return timestamp / rollup.rollup * rollup.rollup
  }
}
