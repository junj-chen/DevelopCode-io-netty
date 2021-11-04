package netty.c3;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

                    sc.register(selector, SelectionKey.OP_READ);

                }else if(key.isReadable()){
                    // 读事件

                    try {
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        int read = channel.read(buffer);  // 如果客户端正常下线，读取的数据是 -1
                        if(read > 0){
                            // 正常处理
                            buffer.flip();
                            log.debug("读取的数据是： {}", new String(buffer.array(), 0, buffer.remaining()));
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

}
