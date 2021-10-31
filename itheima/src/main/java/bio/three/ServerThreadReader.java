package bio.three;

/**
 * @title: ServerThreadReader
 * @Description //TODO
 * @Author Chen
 * @Date: 2021/10/30 11:08
 * @Version 1.0
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 服务端读操作线程处理器
 */
public class ServerThreadReader extends Thread {

    // 与客户端相连接的socket
    private Socket socket;

    public ServerThreadReader(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {

        try {
            // socket接收消息
            InputStream is = socket.getInputStream();

            BufferedReader bs = new BufferedReader(new InputStreamReader(is));

            String msg;

            while((msg = bs.readLine()) != null){
                System.out.println(Thread.currentThread().getName() + " 读取消息： " + msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
