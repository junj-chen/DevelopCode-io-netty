package netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName EchoClient.java
 * @Description 客户端实现： 向服务端发送数据，同时接受服务端转发的数据
 * @createTime 2021年11月08日 15:42:00
 */
@Slf4j
public class EchoClient {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();

        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));

                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                // 添加入栈消息的处理器
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("客户端接受的消息是： {}", buf.toString(Charset.defaultCharset()));
//                                System.out.println(buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .connect(new InetSocketAddress("localhost", 8899))
                .sync()
                .channel();


        ChannelFuture closeFuture = channel.closeFuture().addListener(future ->{
            log.debug("关闭处理");
            group.shutdownGracefully();  // 关闭连接
        });

        // 开启线程进行消息的发送
        new Thread(()->{

            Scanner scanner = new Scanner(System.in);

            while (true){
                String line = scanner.nextLine();
                if(line.equals("q")){
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();

    }
}
