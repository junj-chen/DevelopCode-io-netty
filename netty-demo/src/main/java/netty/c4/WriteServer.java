package netty.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @title: WriteServer
 * @Description // 演示 NIO 的 write事件
 * @Author Chen
 * @Date: 2021/11/4 20:12
 * @Version 1.0
 */
public class WriteServer {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8888));

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true){

            selector.select(); // 阻塞事件
            Iterator<SelectionKey> itr = selector.selectedKeys().iterator();

            while (itr.hasNext()){

                SelectionKey key = itr.next();
                itr.remove();  // 移除事件

                if(key.isAcceptable()){
                    // 客户端的连接事件
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);

                    SelectionKey sck = sc.register(selector, SelectionKey.OP_READ, null);

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < 30000000; i++) {
                        sb.append("a");
                    }
                     ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    // 发送数据
                    int write = sc.write(buffer);
                    System.out.println(write);

                    /**
                     * 出现阻塞的情况，防止该情况的发生，使用 write 事件
                     while(buffer.hasRemaining()){
                         // 没有写完
                         write += sc.write(buffer);
                         System.out.println(write);
                     }
                     */

                    if(buffer.hasRemaining()){
                        // 如果没有发送完， 注册一个 写事件，进行数据的发送，防止出现阻塞情况
                        sck.interestOps(sck.interestOps() + SelectionKey.OP_WRITE); // 保证以前注册事件 和 新注册写事件
                        sck.attach(buffer); // 将buffer 与 sc进行绑定
                    }
                } else if (key.isWritable()){
                    // 如果读事件
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    int write = channel.write(buffer);
                    System.out.println(write);

                    // 如果没有数据可以写入
                    if(!buffer.hasRemaining()){
                        key.attach(null);
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }
                }

            }


        }

    }


}
