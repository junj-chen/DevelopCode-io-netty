package bio.portdispatcher;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @title: Server
 * @Description // 实现 服务端接收客户端的信号，将信号转发给每一个客户端
 * @Author Chen
 * @Date: 2021/10/31 10:10
 * @Version 1.0
 */
public class Server {

    // 全局保存所有在线的 socket
    public static List<Socket> serverSocketOnlineList = new ArrayList<>();

    public static void main(String[] args) {


        try {

            // 1. 注册ServerSocket,等待客户端连接
            ServerSocket ss = new ServerSocket(9999);

            while(true){

                // 2. 循环中阻塞等待客户端连接
                Socket socket = ss.accept();

                // 2.1 将socket加入全局的 List 保存
                serverSocketOnlineList.add(socket);

                // 3. 开启线程进行处理
                new ServerSocketThread(socket).start();

            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
