package netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName HelloWorldClient.java
 * @Description 客户端演示发送数据，解决粘包、半包问题
 * @createTime 2021年11月09日 09:36:00
 */
public class HelloWorldClient {

    static final Logger log = LoggerFactory.getLogger(HelloWorldClient.class);

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            send();
        }


    }

    private static void send() {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    log.debug("connected...");

                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        // channel 建立的时候
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            log.debug("sending...");

                                ByteBuf buffer = ctx.alloc().buffer();
                                buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,18});
                                ctx.writeAndFlush(buffer);

                                // 发完即关, 解决粘包问题
                                ctx.close();

                        }
                    });
                }
            });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8889).sync();

            channelFuture.channel().closeFuture().sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }


}
