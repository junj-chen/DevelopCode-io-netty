package niochat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @title: Client
 * @Description // NIO 通信客户端实现
 * @Author Chen
 * @Date: 2021/10/31 21:16
 * @Version 1.0
 */
public class Client {

    private Selector selector;
    private SocketChannel socketChannel;
    private static int PORT = 9999;


    public Client(){

        try {
            // 基本的初始化
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", PORT));
            socketChannel.configureBlocking(false);

            // 注册客户端 socketChannel到选择器
            // 客户端一般用于接收 服务端发送的消息，所以类型为读
            socketChannel.register(selector, SelectionKey.OP_READ);

            System.out.println("客户端初始化成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {

        Client client = new Client();

        // 开启客户端线程进行接收服务端发送过来的消息
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // 处理服务端发送的消息
                    client.handlerServerMsg();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        Scanner scanner = new Scanner(System.in);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (scanner.hasNextLine()){
            System.out.print("请输入： " );

            String msg = scanner.nextLine();

            buffer.put(msg.getBytes(StandardCharsets.UTF_8));

            buffer.flip();

            client.socketChannel.write(buffer);

            buffer.clear();

        }

    }

    /**
     * 用于接收服务端发送过来的消息
     */
    private void handlerServerMsg() throws IOException {

        while (selector.select() > 0){

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while(it.hasNext()){
                // 获取事件
                SelectionKey key = it.next();
                if(key.isReadable()){

                    SocketChannel sChannel = (SocketChannel)key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    sChannel.read(buffer);

                    buffer.flip();

                    System.out.println("读取到的数据是 " + new String(buffer.array(), 0 , buffer.remaining()));

                    buffer.clear();
                }

                // 去除事件
                it.remove();
            }
        }
    }


}
