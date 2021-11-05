package nio.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @title: WriteClient
 * @Description // NIO write事件 客户端的实现
 * @Author Chen
 * @Date: 2021/11/4 20:20
 * @Version 1.0
 */
public class WriteClient {

    public static void main(String[] args) throws IOException {

         SocketChannel sc = SocketChannel.open();
         sc.connect(new InetSocketAddress("127.0.0.1", 8888));
         ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
         int read = 0;
         while (true){
             read += sc.read(buffer);
             System.out.println(read);
             buffer.clear();  // 进行清除
         }

    }

}
