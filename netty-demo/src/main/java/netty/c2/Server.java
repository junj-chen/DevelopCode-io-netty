package netty.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @title: Server
 * @Description // NIO 实现服务端(阻塞模式)
 * @Author Chen
 * @Date: 2021/11/3 22:23
 * @Version 1.0
 */

@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ByteBuffer buffer = ByteBuffer.allocate(16);

        ssc.bind(new InetSocketAddress(8888));

        List<SocketChannel> channels = new ArrayList<>();

        while(true){
            log.debug("before Connecting...");
            final SocketChannel sc = ssc.accept();  // 阻塞的方法
            log.debug("connected...{}", sc);
            channels.add(sc);

            for(SocketChannel channel : channels){

                log.debug("before read...{}", channel);
                channel.read(buffer);  // 阻塞的方法

                buffer.flip();
                log.debug("content: {}", new String(buffer.array(), 0, buffer.remaining()));

                buffer.clear();
                log.debug("after read...{}", channel);

            }



        }



    }


}
