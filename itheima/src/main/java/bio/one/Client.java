package bio.one;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @title: Client
 * @Description //TODO
 * @Author Chen
 * @Date: 2021/10/27 21:53
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) {

        try {
            // 1.客户端建立socket
            Socket socket = new Socket("127.0.0.1", 9999);

            //2. 获取输出流
            OutputStream outputStream = socket.getOutputStream();

            PrintStream printStream = new PrintStream(outputStream);

            printStream.println("hello server");

            printStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
