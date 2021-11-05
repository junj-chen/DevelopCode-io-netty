package netty.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @title: SignelClient
 * @Description //TODO
 * @Author Chen
 * @Date: 2021/11/4 23:27
 * @Version 1.0
 */
public class SignelClient {

    public static void main(String[] args) throws IOException {

        SocketChannel sc = SocketChannel.open();

        sc.connect(new InetSocketAddress("127.0.0.1", 8885));

//        System.out.println("watting....");

        sc.write(Charset.defaultCharset().encode("12345678fff"));

        System.in.read();

    }

}
