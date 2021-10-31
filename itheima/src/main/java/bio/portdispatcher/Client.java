package bio.portdispatcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @title: Client
 * @Description //客户端实现发送消息和接收服务端的消息
 * @Author Chen
 * @Date: 2021/10/31 10:28
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) {


        try {

            // 1. 建立客户端与服务端的连接
            Socket socket = new Socket("127.0.0.1", 9999);

            OutputStream os = socket.getOutputStream();

            DataOutputStream dos = new DataOutputStream(os);

            Scanner scanner = new Scanner(System.in);

            // 开启线程接收服务端发送过来的消息
            new ClientSocketThread(socket).start();


            while (true){

                System.out.print(Thread.currentThread().getName() + " 请说： ");
                String line = scanner.nextLine();
                // 发送消息给服务端
                dos.writeUTF(line);
                dos.flush();
            }


        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
