package com.graphene.writer.input.graphite

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import net.engio.mbassy.bus.MBassador
import net.iponweb.disthene.config.DistheneConfiguration
import net.iponweb.disthene.events.DistheneEvent
import org.apache.log4j.Logger
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * @author Andrei Ivanov
 */
@Component
class CarbonServer(private val configuration: DistheneConfiguration, private val bus: MBassador<DistheneEvent>) {
  private val logger = Logger.getLogger(CarbonServer::class.java)

  private val bossGroup = NioEventLoopGroup()
  private val workerGroup = NioEventLoopGroup()

  @PostConstruct
  @Throws(InterruptedException::class)
  fun run() {
    val b = ServerBootstrap()
    b.group(bossGroup, workerGroup)
      .channel(NioServerSocketChannel::class.java)
      .option(ChannelOption.SO_BACKLOG, 100)
      .childHandler(object : ChannelInitializer<SocketChannel>() {
        @Throws(Exception::class)
        public override fun initChannel(ch: SocketChannel) {
          val p = ch.pipeline()
          p.addLast(DelimiterBasedFrameDecoder(MAX_FRAME_LENGTH, false, *Delimiters.lineDelimiter()))
          p.addLast(CarbonServerHandler(bus, configuration.carbon.baseRollup))
        }

        @Throws(Exception::class)
        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
          logger.error(cause)
          super.exceptionCaught(ctx, cause)
        }
      })

    // Start the server.
    b.bind(configuration.carbon.bind, configuration.carbon.port).sync()
  }

  @PreDestroy
  fun shutdown() {
    logger.info("Shutting down boss group")
    bossGroup.shutdownGracefully().awaitUninterruptibly(60000)

    logger.info("Shutting down worker group")
    workerGroup.shutdownGracefully().awaitUninterruptibly(60000)
  }

  companion object {
    private const val MAX_FRAME_LENGTH = 8192
  }
}
