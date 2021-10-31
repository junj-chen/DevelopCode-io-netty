package selector.one;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @title: Server
 * @Description // NIO 服务端的代码
 * @Author Chen
 * @Date: 2021/10/31 15:31
 * @Version 1.0
 */
public class Server {


    public static void main(String[] args) throws IOException {

        System.out.println("服务端启动----");

        // 1. 创建服务端的 channel
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // 2. 设置channel为非阻塞的
        ssChannel.configureBlocking(false);

        // 3.绑定服务端的端口
        ssChannel.bind(new InetSocketAddress(9999));

        // 4. 获取一个 Selector
        Selector selector = Selector.open();

        // 5. 将channel 注册到selector上
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 6. 阻塞轮训等待客户端连接
        while(selector.select() > 0){  // 阻塞的方法，有就绪的事件
            System.out.println("开启一轮事件处理**");
            // 7. 获取选择器中所有注册的通道中已经就绪好的事件
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            // 8. 遍历处理已经就绪好的事件
            while(it.hasNext()){
                // 9. 提取需要处理的事件
                SelectionKey key = it.next();

                // 10. 判断处理的事件类型，进行处理
                if(key.isAcceptable()){
                    // 11. 连接事件，服务端与客户端的连接
                    SocketChannel sChannel = ssChannel.accept();
                    // 12. 设置 非阻塞模式
                    sChannel.configureBlocking(false);
                    // 13. 将改事件注册到 选择器中，设置为 读模式
                    sChannel.register(selector, SelectionKey.OP_READ); // 等待客户端发送过来的数据
                }else if(key.isReadable()){
                    // 读事件
                    // 14. 获取当前选择器中读就绪的channel
                    SocketChannel sChannel = (SocketChannel)key.channel();

                    // 15. 分配缓冲区
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len;
                    // 16. 读取channel中的数据到缓冲区中
                    while((len = sChannel.read(buffer)) > 0){

                        // 17. 切换缓冲区的模式为读模式
                        buffer.flip();

                        String msg = new String(buffer.array(), 0, len);

                        System.out.println("接收到的数据为： " + msg);

                        // 18. 清除缓冲区，重新接收数据
                        buffer.clear();
                    }


                }


                // 事件处理完毕，需要移除当前事件
                it.remove();
            }

        }


    }

}
