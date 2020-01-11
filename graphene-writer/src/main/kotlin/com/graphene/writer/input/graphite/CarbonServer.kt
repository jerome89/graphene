package com.graphene.writer.input.graphite

import com.graphene.writer.input.graphite.property.InputGraphiteProperty
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

/**
 * @author Andrei Ivanov
 * @author dark
 */
@Component
class CarbonServer(
  private val graphiteProperty: InputGraphiteProperty,
  private val carbonServerHandler: CarbonServerHandler
) {

  private val logger = LogManager.getLogger(CarbonServer::class.java)

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
          p.addLast(carbonServerHandler)
        }

        @Throws(Exception::class)
        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
          logger.error(cause)
          super.exceptionCaught(ctx, cause)
        }
      })

    // Start the server.
    b.bind(graphiteProperty.inputGraphiteCarbonProperty.bind, graphiteProperty.inputGraphiteCarbonProperty.port).sync()
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
