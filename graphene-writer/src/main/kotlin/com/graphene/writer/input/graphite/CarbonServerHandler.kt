package com.graphene.writer.input.graphite

import com.google.common.base.CharMatcher
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.CharsetUtil
import net.engio.mbassy.bus.MBassador
import net.iponweb.disthene.bean.Metric
import net.iponweb.disthene.config.Rollup
import net.iponweb.disthene.events.DistheneEvent
import net.iponweb.disthene.events.MetricReceivedEvent
import org.apache.log4j.Logger

/**
 * @author Andrei Ivanov
 */
class CarbonServerHandler(private val bus: MBassador<DistheneEvent>, private val rollup: Rollup) : ChannelInboundHandlerAdapter() {
  private val logger = Logger.getLogger(CarbonServerHandler::class.java)

  @Throws(Exception::class)
  override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
    val byteBuf = msg as ByteBuf

    try {
      val metric = Metric(byteBuf.toString(CharsetUtil.UTF_8).trim { it <= ' ' }, rollup)
      if (System.currentTimeMillis() / 1000L - metric.timestamp > 3600) {
        logger.warn("Metric is from distant past (older than 1 hour): $metric")
      }

      if (CharMatcher.ASCII.matchesAllOf(metric.path) && CharMatcher.ASCII.matchesAllOf(metric.tenant)) {
        bus.post(MetricReceivedEvent(metric)).now()
      } else {
        logger.warn("Non ASCII characters received, discarding: $metric")
      }
    } catch (e: Exception) {
      logger.trace(e)
    }

    byteBuf.release()
  }
}
