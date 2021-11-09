package netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName HelloWorldServer.java
 * @Description 服务端，演示解决粘包、半包问题
 * @createTime 2021年11月09日 09:48:00
 */
public class HelloWorldServer {

    static final Logger log = LoggerFactory.getLogger(HelloWorldServer.class);

    void start(){

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);

            // 调整系统的接收缓冲区（滑动窗口）
//            serverBootstrap.option(ChannelOption.SO_RCVBUF, 10);

            // 调整 netty 的接收缓冲区 （ByteBuf）
            serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16));

            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

//                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder());  // 解码器（协议）


                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            log.debug("connected..... {}", ctx.channel());
                            super.channelActive(ctx);
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            log.debug("disconnected {}", ctx.channel());
                            super.channelInactive(ctx);
                        }
                    });
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind(8889);
            log.debug("{} binding...", channelFuture.channel());

            channelFuture.sync();
            log.debug("{} bound....", channelFuture.channel());

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
           log.debug("server error {}", e);
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            log.debug("stopped");
        }
    }


    public static void main(String[] args) {

        new HelloWorldServer().start();

    }


}
