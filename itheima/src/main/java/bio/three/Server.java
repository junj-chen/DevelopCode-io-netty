package bio.three;

/**
 * @title: Server
 * @Description //TODO
 * @Author Chen
 * @Date: 2021/10/30 11:04
 * @Version 1.0
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
   目标：服务端 实现 开启多个线程接受客户端消息
 */
public class Server {

    public static void main(String[] args) {

        try {
            // 1. 建立ServerSocket ，监听客户端
            ServerSocket ss = new ServerSocket(9999);

            while(true){
                // 2. 开启循环，监听客户端连接
                Socket sc = ss.accept();  // 阻塞

                // 3. 开启服务端线程进行消息的处理
                new ServerThreadReader(sc).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
