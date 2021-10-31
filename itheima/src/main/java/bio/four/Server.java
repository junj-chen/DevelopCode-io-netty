package bio.four;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @title: Server
 * @Description // 开发 实现伪异步通信架构 服务端（使用线程池）
 * @Author Chen
 * @Date: 2021/10/30 11:30
 * @Version 1.0
 */
public class Server {


    public static void main(String[] args) {


        try {
            // 1.建立ServerSocket
            ServerSocket ss = new ServerSocket(9999);

            // 创建一个一个线程池
            HanderServerSocketPool pools = new HanderServerSocketPool(6, 10);

            // 2. 使用循环等待客户端连接
            while (true){

                Socket socket = ss.accept();

                // 3. 将socket包装成一个任务，放到线程池执行
                ServerRunnabelTask task = new ServerRunnabelTask(socket);
                pools.execute(task);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
