package netty.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @title: EventLoopServer
 * @Description // 服务端
 * @Author Chen
 * @Date: 2021/11/7 12:57
 * @Version 1.0
 */
@Slf4j
public class EventLoopServer {

    public static void main(String[] args) {

        // 2. 指定一个独立的 EventLoop进行耗时任务等的执行
        DefaultEventLoop group = new DefaultEventLoop();

        // 1. 创建服务
        new ServerBootstrap()
                // 参数1 关注accept事件发送， 参数2 work发生时使用
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("handel-1", new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug(buf.toString(Charset.defaultCharset()));

                                // 将消息传递到下一个 handler
                                ctx.fireChannelRead(msg);
                            }
                        })
                        // 添加第二个 Handler 进行耗时任务的执行，使用一个 独立的 EventLoop
                        .addLast(group,"handler-2",  new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug(buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8888);
    }

}
