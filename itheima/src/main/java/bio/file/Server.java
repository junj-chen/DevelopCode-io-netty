package bio.file;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @title: Server
 * @Description // 服务端以多线程的方式接收客户端的数据，进行保存
 * @Author Chen
 * @Date: 2021/10/30 20:42
 * @Version 1.0
 */
public class Server {

    public static void main(String[] args) {

        try {

            // 1. 建立ServerSocket，等待客户端的连接
            ServerSocket ss = new ServerSocket(8888);

            while (true){
                // 客户端的连接
                Socket socket = ss.accept();

                // 2. 使用多线程处理客户端的连接
                new ServerReaderThread(socket).start();

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
