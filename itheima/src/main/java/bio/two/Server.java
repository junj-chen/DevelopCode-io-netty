package bio.two;

/**
 * @title: Server
 * @Description //TODO
 * @Author Chen
 * @Date: 2021/10/27 22:27
 * @Version 1.0
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  目标： 实现多发多收的机制
 */
public class Server {

    public static void main(String[] args) {

        try {
            System.out.println("服务端启动成功");
            //1. 建立 serverSocket
            ServerSocket sc = new ServerSocket(9999);

            //2. 阻塞等待客户端的连接
            Socket ac = sc.accept();

            //3. 获取输入流
            InputStream is = ac.getInputStream();

            //4. 包装输入流，
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String msg;
            while((msg = br.readLine()) != null){

                System.out.println("服务端接收到： " + msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
