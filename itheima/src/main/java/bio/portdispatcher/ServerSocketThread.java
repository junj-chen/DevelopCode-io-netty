package bio.portdispatcher;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * @title: ServerSocketThread
 * @Description // 服务端线程处理
 * @Author Chen
 * @Date: 2021/10/31 10:15
 * @Version 1.0
 */
public class ServerSocketThread extends Thread{

    private Socket socket;


    public ServerSocketThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            // 获取socket输入流
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String msg;
            while((msg = dis.readUTF()) != null){

                // 接收到消息，将进行转发

                // 实现消息的转发
                sendToAllOnlineSocket(Thread.currentThread().getName() + " 发送的消息： " + msg);

            }


        } catch (IOException e) {

            try {
                System.out.println("socket下线，移除");
                Server.serverSocketOnlineList.remove(socket);

            }catch (Exception ex){
                ex.printStackTrace();
            }

            e.printStackTrace();
        }


    }

    private void sendToAllOnlineSocket(String msg) {

        try {

            if (Server.serverSocketOnlineList != null && Server.serverSocketOnlineList.size() != 0){

                for(Socket soc : Server.serverSocketOnlineList){
                    // 得到输出流
                    OutputStream os = soc.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);

                    dos.writeUTF(msg);
                    dos.flush();
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
