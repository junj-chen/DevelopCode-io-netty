package bio.file;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * @title: ServerReaderThread
 * @Description // 服务端进行数据处理的线程逻辑
 * @Author Chen
 * @Date: 2021/10/30 20:46
 * @Version 1.0
 */
public class ServerReaderThread extends Thread{
    // 客户端与服务端建立的 socket 连接
    private Socket socket;

    public ServerReaderThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            // 1. 通过 socket 接收服务端发送的数据
            InputStream is = socket.getInputStream();
            // 数据输入流
            DataInputStream dis = new DataInputStream(is);

            // 接收文件后缀
            String prefix = dis.readUTF();

            System.out.println("服务端接收到文件后缀..");

            FileOutputStream fos = new FileOutputStream("C:\\Users\\JunjieChen\\Desktop\\测试\\server\\" + UUID.randomUUID().toString() + prefix);
            byte[] buffer = new byte[1024];
            int len;
            while((len = dis.read(buffer)) > 0){
                // 写入文件
                fos.write(buffer, 0, len);
            }
            System.out.println("服务端数据已经接收完毕");
            fos.close();
            System.out.println("服务端正确关闭");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
