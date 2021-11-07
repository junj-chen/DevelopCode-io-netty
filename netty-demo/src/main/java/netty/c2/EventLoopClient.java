package netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @title: EventLoopClient
 * @Description // 客户端
 * @Author Chen
 * @Date: 2021/11/7 13:01
 * @Version 1.0
 */
@Slf4j
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException {

        // 1.获取 ChannelFuture 对象
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 1. 连接到服务器，异步非阻塞的方式，真正的执行 connect的是 NIO线程
                .connect(new InetSocketAddress("localhost", 8888));

        /**
         * 2. 两种方式对 建立连接后 客户端进行操作
         *  1. sync() 方法： 同步阻塞方法，等待连接建立后，调用的线程（main）进行消息发送
         *      [main] DEBUG netty.c2.EventLoopClient
         *
         *  2. 添加 addListener （回调对象） 方法进行异步处理对象， 使用 NIO的线程进行消息的发送
         *      [nioEventLoopGroup-2-1] DEBUG netty.c2.EventLoopClient
         */

        // 2.1 sync()方法同步处理结果
        channelFuture.sync();  // 阻塞，等待连接建立
        // 非阻塞的直接运行
        Channel channel = channelFuture.channel();
        log.debug("{}",channel);

        // 2.2 添加 addListener （回调对象） 方法进行异步处理对象
/*        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.debug("channel****************************************************: {}",channel);
                channel.writeAndFlush("helllo0 ");
                System.out.println("jjjjj");
            }
        });*/



    }

}
