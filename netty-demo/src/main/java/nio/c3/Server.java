package nio.c3;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName Server.java
 * @Description TODO
 * @createTime 2021年11月04日 10:16:00
 */

@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel ssc = ServerSocketChannel.open();
        Selector selector = Selector.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8887));

        SelectionKey sscK = ssc.register(selector, 0, null);

        sscK.interestOps(SelectionKey.OP_ACCEPT);  // 设置 accept事件

        while(true){
            selector.select();  // 阻塞方法，等待有事件返回
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();  // 返回一个集合，该集合是存储触发的事件
            // 遍历事件key
            while(iter.hasNext()){
                SelectionKey key = iter.next();
                log.debug("key...{}", key);
                if(key.isAcceptable()){
                    // 如果是 客户端连接事件
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel sc = channel.accept();
                    log.debug("注册的key: {}", sc);
                    // 配置 客户端非阻塞模式
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    sc.register(selector, SelectionKey.OP_READ, buffer);  // 将 buffer作为一个 att 属性

                }else if(key.isReadable()){
                    // 读事件
                    try {
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();  // 获取对应的属性

                        int read = channel.read(buffer);  // 如果客户端正常下线，读取的数据是 -1
                        if(read > 0){
                            // 正常处理, 切分数据
                            split(buffer);

                            // 判断时候需要扩容
                            if (buffer.position() == buffer.limit()){
                                // 需要进行扩容
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                // 将buffer中的字节进行 拷贝
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }else {
                            // 客户端正常退出的情况，将事件进行取消
                            key.cancel();
                        }

                    }catch (Exception e){
                        // 出现异常，客户端异常下线情况
                        key.cancel();
                        log.debug("客户端异常下线...{}", key);
                    }
                }
                // 在 迭代器 集合中的事件处理完成后必须进行移除
                iter.remove();
            }
        }
    }

    /**
     * 切分数据，以 ‘\n’进行数据的切分
     * @param buffer
     */

    private static void split(ByteBuffer buffer) {

        buffer.flip(); // 切换为读模式

        for(int i = 0; i < buffer.limit(); i++){

            if(buffer.get(i) == '\n'){
                // 找到分隔符
                int len = i + 1 - buffer.position();
                ByteBuffer target = ByteBuffer.allocate(len);  //分配新的长度的 buffer

                for (int j = 0; j < len; j++) {
                    target.put(buffer.get());  // 写入数据
                }
                target.flip();
                log.debug("读取的数据是： {}", new String(target.array(), 0, target.remaining()));
            }

        }

        buffer.compact(); // 移动位置,切换为写模式
    }


}
