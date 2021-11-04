package netty.c3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @title: Client
 * @Description // NIO 客户端
 * @Author Chen
 * @Date: 2021/11/3 22:30
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) throws IOException {

        final SocketChannel sc = SocketChannel.open();

        sc.connect(new InetSocketAddress("127.0.0.1", 8887));

//        System.out.println("watting....");

        sc.write(Charset.defaultCharset().encode("12345678\n9abcdefgfffffffffffffffff\n"));

        System.in.read();

    }

}
