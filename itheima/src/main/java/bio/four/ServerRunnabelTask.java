package bio.four;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @title: ServerRunnabelTask
 * @Description // 服务端任务执行
 * @Author Chen
 * @Date: 2021/10/30 13:02
 * @Version 1.0
 */
public class ServerRunnabelTask implements Runnable {

    private Socket socket;

    public ServerRunnabelTask(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader ps = new BufferedReader(new InputStreamReader(is));
            String msg;
            while((msg = ps.readLine()) != null){
                System.out.println(Thread.currentThread().getName() + " 读取： " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
