package bio.four;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @title: Client
 * @Description  客户端 实现消息发送 服务端
 * @Author Chen
 * @Date: 2021/10/30 11:15
 * @Version 1.0
 */

public class Client {

    public static void main(String[] args) {

        try {
            // 1. 建立socket连接
            Socket socket = new Socket("127.0.0.1", 9999);

            OutputStream os = socket.getOutputStream();

            PrintStream ps = new PrintStream(os);

            Scanner sc = new Scanner(System.in);

            while (true){

                System.out.print("请输入： ");
                String line = sc.nextLine();

                ps.println(line);
                ps.flush();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
