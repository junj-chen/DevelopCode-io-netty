package netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName TestHttpServer.java
 * @Description netty 编写 Http server端的消息处理
 * @createTime 2021年11月09日 15:59:00
 */

@Slf4j
public class TestHttpServer {


    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new HttpServerCodec());  // 添加 Http Server端的 请求解码器 以及 响应编码器

                    // 该接口可以指定 对处理消息的类型
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                            // 指定对 HttpRequest 的消息进行处理

                            // 1. 获取请求
                            log.debug(msg.uri());

                            // 2. 返回响应
                            DefaultFullHttpResponse response = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);

                            byte[] bytes = "<h1>Hello, world!</h1>".getBytes();

                            response.headers().setInt(CONTENT_LENGTH, bytes.length);
                            response.content().writeBytes(bytes);

                            ctx.writeAndFlush(response);
                        }
                    });

                    /* // 对消息进行处理， 使用统一适配器进行处理比较麻烦
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("接收到的消息....... {}", msg.getClass());   // 返回两种类型的小

                            if(msg instanceof HttpRequest){
                                // 请求类型的消息

                            }else if (msg instanceof HttpContent){
                                // 消息体类型

                            }

                        }
                    });*/

                }
            });
            ChannelFuture channelFuture = bootstrap.bind(8887).sync();
            channelFuture.channel().closeFuture().sync();  // 关闭阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }
    


}
