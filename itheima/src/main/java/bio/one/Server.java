package bio.one;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @title: Server
 * @Description //TODO
 * @Author Chen
 * @Date: 2021/10/27 21:53
 * @Version 1.0
 */
public class Server {

    public static void main(String[] args) {

        try {
            //1. 创建serverSocket
            ServerSocket ss = new ServerSocket(9999);

            System.out.println("服务端开启**");

            //2. 阻塞等待客户端连接
            Socket sc = ss.accept();

            //3. 创建输入流接收客户端发送的消息
            InputStream is = sc.getInputStream();

            BufferedReader bs = new BufferedReader(new InputStreamReader(is));

            String msg;

            if((msg = bs.readLine()) != null){
                System.out.println("服务端接收到： " + msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
