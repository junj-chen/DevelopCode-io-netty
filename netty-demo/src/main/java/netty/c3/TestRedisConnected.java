package netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName TestRedisConnected.java
 * @Description 使用 netty，模拟 redis 数据格式进行 redis命令的发送
 * @createTime 2021年11月09日 11:31:00
 */
public class TestRedisConnected {

    public static void main(String[] args) {

        final byte[] LINE = {13, 10};  // 换行符

        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            Channel channel = bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 模拟 发送命令到redis
                            setCommand(ctx);

                            // 模拟 get 命令
                            getCommand(ctx);

                        }

                        private void getCommand(ChannelHandlerContext ctx){

                            ByteBuf buf = ctx.alloc().buffer();
                            buf.writeBytes("*2".getBytes());
                            buf.writeBytes(LINE);
                            buf.writeBytes("$3".getBytes());
                            buf.writeBytes(LINE);
                            buf.writeBytes("get".getBytes());
                            buf.writeBytes(LINE);
                            buf.writeBytes("$7".getBytes());
                            buf.writeBytes(LINE);
                            buf.writeBytes("testKey".getBytes());
                            buf.writeBytes(LINE);

                            ctx.writeAndFlush(buf);

                        }


                        private void setCommand(ChannelHandlerContext ctx) {
                            ByteBuf buffer = ctx.alloc().buffer();
                            buffer.writeBytes("*3".getBytes());
                            buffer.writeBytes(LINE);
                            buffer.writeBytes("$3".getBytes());
                            buffer.writeBytes(LINE);
                            buffer.writeBytes("set".getBytes());
                            buffer.writeBytes(LINE);
                            buffer.writeBytes("$7".getBytes());
                            buffer.writeBytes(LINE);
                            buffer.writeBytes("testKey".getBytes());
                            buffer.writeBytes(LINE);
                            buffer.writeBytes("$7".getBytes());
                            buffer.writeBytes(LINE);
                            buffer.writeBytes("testVal".getBytes());
                            buffer.writeBytes(LINE);

                            ctx.writeAndFlush(buffer);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            // 接收 服务端返回的消息
                            ByteBuf buf = (ByteBuf) msg;

                            System.out.println("服务端返回的消息是： " + buf.toString(Charset.defaultCharset()));

                        }
                    });
                }
            }).connect("localhost", 6379).sync().channel();

            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }


    }

}
