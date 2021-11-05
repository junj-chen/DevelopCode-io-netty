package netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName HelloClient.java
 * @Description Netty 客户端实现
 * @createTime 2021年11月05日 15:52:00
 */
public class HelloClient {

    public static void main(String[] args) throws InterruptedException {

        // 1. 启动器
        new Bootstrap()
                // 2. 添加EventLoop
                .group(new NioEventLoopGroup())
                // 3. 选择客户端的 channel
                .channel(NioSocketChannel.class)
                // 4. 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override  // 连接建立后使用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 5. 连接到服务器
                .connect(new InetSocketAddress("127.0.0.1", 8888))
                .sync()
                .channel()
                // 6. 发送数据
                .writeAndFlush("hello word");

    }

}
