package netty.c2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @title: TestEmbededChannel
 * @Description // 测试入栈 出站处理器的顺序
 * @Author Chen
 * @Date: 2021/11/7 19:51
 * @Version 1.0
 */
@Slf4j
public class TestEmbededChannel {

    public static void main(String[] args) {

        ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("1");
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter h2 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("2");
                super.channelRead(ctx, msg);
            }
        };

        ChannelOutboundHandlerAdapter h3 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug("3");
                super.write(ctx, msg, promise);
            }
        };

        ChannelOutboundHandlerAdapter h4 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug("4");
                super.write(ctx, msg, promise);
            }
        };


        EmbeddedChannel channel = new EmbeddedChannel(h1, h3, h2,  h4);

        // 模拟入栈
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("helll".getBytes(StandardCharsets.UTF_8)));

        System.out.println("********************************");

        // 模拟出站
        channel.writeOutbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("helll".getBytes(StandardCharsets.UTF_8)));



    }


}
