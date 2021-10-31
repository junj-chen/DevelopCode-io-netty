package bio.two;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @title: Client
 * @Description //TODO
 * @Author Chen
 * @Date: 2021/10/27 22:27
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) {

        try {
            //1. 建立socket连接
            Socket socket = new Socket("127.0.0.1", 9999);

            //2. 获取输出流
            OutputStream os = socket.getOutputStream();

            //3. 包装输出流
            PrintStream ps = new PrintStream(os);

            Scanner scanner = new Scanner(System.in);

            while(true){

                System.out.print("输入： ");
                String line = scanner.nextLine();
                ps.println(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
