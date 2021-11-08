package netty.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName EchoServer.java
 * @Description 服务端代码，实现接收数据返回客户端
 * @createTime 2021年11月08日 15:33:00
 */
@Slf4j
public class EchoServer {

    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                        // 添加入栈消息处理
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("接收到消息：{}", buf.toString(Charset.defaultCharset()));

                                // 创建一个新的 ByteBuf
                                ByteBuf response = ctx.alloc().buffer();
                                // 新的ByteBuf 中写入 数据
                                response.writeBytes(buf);
                                // 使用通道进行数据传输
                                ctx.writeAndFlush(response);
                            }
                        });
                    }
                })
                .bind(8899);

    }
}
