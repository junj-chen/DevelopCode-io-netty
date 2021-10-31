package bio.portdispatcher;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @title: ClientSocketThread
 * @Description // 开启客户端线程接收服务端发送的消息
 * @Author Chen
 * @Date: 2021/10/31 10:36
 * @Version 1.0
 */
public class ClientSocketThread extends Thread {

    private Socket socket;

    public ClientSocketThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            // 1. 获取socket连接
            InputStream is = socket.getInputStream();

            // 2. 数据流进行包装
            DataInputStream dis = new DataInputStream(is);

            while (true){
                // 循环等待服务端发送的数据
                String msg = dis.readUTF();

                System.out.println(msg);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
