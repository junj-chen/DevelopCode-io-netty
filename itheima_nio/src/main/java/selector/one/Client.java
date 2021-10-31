package selector.one;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @title: Client
 * @Description // NIO通信的客户端代码
 * @Author Chen
 * @Date: 2021/10/31 16:04
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) throws IOException {

        // 1. 创建客户端SocketChannel, 绑定端口和IP
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));

        // 2. 配置非阻塞的模式
        sChannel.configureBlocking(false);

        // 3. 申请缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        Scanner scanner = new Scanner(System.in);

        while(true){

            System.out.print("输入数据 ");
            String msg = scanner.nextLine();

            // 4. 将消息放入缓冲区中
            buffer.put((Thread.currentThread().getName() + " " + msg).getBytes(StandardCharsets.UTF_8));

            // 5. 切换缓冲区的模式为读模式
            buffer.flip();

            // 6. 将数据从 buffer中写到 channel中
            sChannel.write(buffer);

            // 7. 清除缓冲区的容量，方便下一次的读写
            buffer.clear();


        }

    }


}
