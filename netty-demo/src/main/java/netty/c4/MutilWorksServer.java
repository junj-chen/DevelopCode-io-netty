package netty.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @title: SingleWorksServer
 * @Description // 简易的 reactor 模型
 * 一个 main线程 负责所有的 accept 事件， 开启多个work线程进行数据的读写
 *
 * client -----------------------------------------------------------------------> 数据交互
 *         连接 -->  main(ServerSocketChannel  ---- selector (accept))  ---->   workThread(socketChannel selector )
 * client -----------------------------------------------------------------------> 数据交互
 *
 * @Author Chen
 * @Date: 2021/11/4 22:23
 * @Version 1.0
 */

@Slf4j
public class MutilWorksServer {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8885));

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        // 1. 初始化多个work线程
        Works[] works = new Works[Runtime.getRuntime().availableProcessors()]; // 获取CPU的可用核心数
        for (int i = 0; i < works.length; i++) {
            works[i] = new Works("work-" + i);
        }

        // 2. 实现一个计数器，进行负载均衡
        AtomicInteger index = new AtomicInteger();

        while(true){
            //
            selector.select(); // 进行阻塞

            Iterator<SelectionKey> itr = selector.selectedKeys().iterator();

            while(itr.hasNext()){

                SelectionKey key = itr.next();
                itr.remove();

                if(key.isAcceptable()){
                    // 客户端注册事件
                    log.debug("等待连接....");
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("before register....");

                    // 负载均衡，分配一个work线程进行处理
                    works[(index.getAndIncrement() % works.length)].register(sc);  // 将channel传进去

                    log.debug("after register....");
                }

            }

        }
    }

    static class Works implements Runnable{
        // 启动线程进行工作
        private Thread work;
        // 独立的 selector
        private Selector selector;
        // 线程的名字
        private String name;

        // 用于指定 一个 works中只能保证有一个 selector
        private volatile boolean flag = false;

        // 消息队列进行 线程间的通信
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        // 构造函数，指定线程的名字
        public Works(String name) {
            this.name = name;
        }

        /**
         * 进行 selector、线程的初始化工作
         */
        public void register(SocketChannel sc) throws IOException {

            if (!flag){
                selector = Selector.open();
                work = new Thread(this, name);  // 初始化线程
                work.start();  // 开启一个新线程等待数据的传输
                flag = true;
            }

            queue.add(()->{
                try {
                    // 有新的客户端进来就进行注册, 将注册的任务封装为一个任务，放到队列中
                    sc.register(selector, SelectionKey.OP_READ, null); // 将通道注册到selector上面

                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });

            // 唤醒 selector,防止下面线程的 selector.select() 阻塞
            selector.wakeup();
        }


        @Override
        public void run() {
            // 等待可读事件的到达
            while (true){

                try {
                    // 进行阻塞
                    selector.select();

                    // 将队列中的任务取出来
                    Runnable task = queue.poll();
                    if (task != null){
                        task.run();  // 执行对 可读事件的注册
                    }

                    Iterator<SelectionKey> itr = selector.selectedKeys().iterator();

                    while (itr.hasNext()){

                        SelectionKey key = itr.next();
                        itr.remove();

                        if(key.isReadable()){

                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            channel.read(buffer);

                            buffer.flip();
                            log.debug("read....{}", new String(buffer.array(), 0, buffer.remaining()));

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
