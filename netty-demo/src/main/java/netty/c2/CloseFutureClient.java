package netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @title:
 * @Description // 关闭客户端
 * @Author Chen
 * @Date: 2021/11/7 15:11
 * @Version 1.0
 */
@Slf4j
public class CloseFutureClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));  // 调试信息
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8889));

        // 阻塞方法
        channelFuture.sync();
        Channel channel = channelFuture.channel();
        log.debug("channel ------- {}", channel);

        // 开启一个线程 向服务端发送消息

        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while (true){

                String line = scanner.nextLine();
                if (line.equals("q")){
                    channel.close();  // 关闭方法是一个异步方法，
//                    log.debug("执行后续操作");  // channel的关闭是一个异步，所以不能在这里进行处理
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();

         /** 3. ChannelFuture 的关闭方式
         * 1. 同步关闭处理, 阻塞到任务结束，由调用者线程进行处理
          *     15:43:42.880 [nioEventLoopGroup-2-1]  [id: 0xe37582bd, L:/127.0.0.1:54345 - R:localhost/127.0.0.1:8889] CLOSE
          *     15:43:42.880 [main] DEBUG netty.c2.CloseFutureClient - 处理关闭之后的操作
          *
          * 2. 异步关闭， 同一个 NIO的线程进行处理
          *     15:49:38.999 [nioEventLoopGroup-2-1]  [id: 0x9dc4a4c0, L:/127.0.0.1:54466 - R:localhost/127.0.0.1:8889] CLOSE
          *     15:49:39.000 [nioEventLoopGroup-2-1] DEBUG netty.c2.CloseFutureClient - 处理关闭之后的操作
          *
         */

/*         // 3.1 同步进行关闭
        ChannelFuture closeFuture = channel.closeFuture();
        log.debug("close ******* {}", closeFuture);
        closeFuture.sync();  // 同步阻塞
        log.debug("处理关闭之后的操作");
        group.shutdownGracefully();  // 关闭事件组 */

        // 3.2 回调关闭
        ChannelFuture closeFuture = channel.closeFuture();
        log.debug("close ******* {}", closeFuture);
        // 添加回调进行处理
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.debug("处理关闭之后的操作");
                group.shutdownGracefully();  // 关闭事件组
            }
        });


    }

}
