package netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName HelloServer.java
 * @Description Netty 服务端代码编写
 * @createTime 2021年11月05日 15:29:00
 */
public class HelloServer {

    public static void main(String[] args) {
        // 1. 启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2. group组，添加 EventLoop
        .group(new NioEventLoopGroup())
                // 3. 选择服务器的 ServerSocketChannel 实现
        .channel(NioServerSocketChannel.class)
                // 4. 处理数据的读写通道 Initial 初始化
        .childHandler(
                // 5. channel代表与客户端进行数据通信的通道
                new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder()); // 将ByteBuf 转换为字符
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                }
        ).bind(8888);
    }
}
