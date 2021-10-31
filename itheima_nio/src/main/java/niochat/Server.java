package niochat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @title: Server
 * @Description // NIO服务端 实现接收客户端的消息，并且转发给每一个客户端
 * @Author Chen
 * @Date: 2021/10/31 16:39
 * @Version 1.0
 */
public class Server {

    // 1. 定义一些属性, 选择器， ServerSocketChannel， PORT
    private Selector selector;
    private ServerSocketChannel ssChannel;

    private static final int PORT = 9999;

    /**
     * 构造函数实现初始化工作
     */
    public Server(){
        // 进行初始化
        try {
            // 1. 初始化 channel
            ssChannel = ServerSocketChannel.open();
            ssChannel.configureBlocking(false); // 非阻塞模式
            ssChannel.bind(new InetSocketAddress(PORT)); // 绑定端口

            // 2. 初始化选择器
            selector = Selector.open();

            // 3. 绑定端口到选择器
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听函数，实现客户端的连接和任务转发
     */
    private void listen() {

        try {
            // 阻塞等待事件的产生
            while (selector.select() > 0){
                // 1. 获取事件
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                while (it.hasNext()){
                    // 获取具体监听到的事件
                    SelectionKey key = it.next();
                    // 2. 如果是客户端连接事件
                    if(key.isAcceptable()){
                        // 2.1 获取客户端的连接
                        SocketChannel sChannel = ssChannel.accept();
                        // 2.2 配置为非阻塞的模式
                        sChannel.configureBlocking(false);
                        // 2.3 将通道注册到选择器中
                        sChannel.register(selector, SelectionKey.OP_READ);
                    }else if(key.isReadable()){
                        // 读事件发生，读取客户端的事件
                        readClientData(key);

                    }
                    // 事件处理完毕后，删除本次事件
                    it.remove();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }




    }

    /**
     * 读取客户端的数据
     * @param key
     */
    private void readClientData(SelectionKey key) {

        SocketChannel sChannel = null;
        try{
            // 1. 获取客户端的 channel
            sChannel = (SocketChannel)key.channel();

            // 2. 分配缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            // 3. 读取数据
            int count = sChannel.read(buffer);

            if(count > 0){
                // 读取到数据
                // buffer 切换为读模式
                buffer.flip();
                String msg = new String(buffer.array(), 0, count);

                System.out.println("服务端接收到数据： " + msg);
                // 将缓冲区清除
                buffer.clear();

                // 4. 将数据转发给所有的端口
                sendMsgToClient(msg, sChannel);
            }

        }catch (Exception e){

            // 发生异常，说明有客户端下线
            try {
                System.out.println("有人离线了 " + sChannel.getRemoteAddress());

                key.cancel();  // 取消这个任务
                sChannel.close();  // 关闭通道
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    /**
     * 转发数据到所有的客户端
     * @param msg
     * @param sChannel
     */
    private void sendMsgToClient(String msg, SocketChannel sChannel) throws IOException {
        System.out.println("服务端开始转发消息，处理的线程是：" + Thread.currentThread().getName());

        // 将消息转发给所有的 客户端
        for(SelectionKey sk : selector.keys()){
            // 获取通道
            Channel sc = sk.channel();
            // 不转发数据给自己 (该 selector 中注册的对象即包括了 服务端的 ServerSocketChannel， 也包括了客户端的 SocketChannel)
            if(sc instanceof SocketChannel && sc != sChannel){

                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                ((SocketChannel)sc).write(buffer);
            }

        }


    }


    public static void main(String[] args) {

        // 1. 实例化 服务器
        Server server = new Server();

        server.listen();

    }




}
