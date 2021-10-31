package bio.file;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * @title: Client
 * @Description //上传任意类型的文件
 * @Author Chen
 * @Date: 2021/10/30 20:34
 * @Version 1.0
 */
public class Client {

    public static void main(String[] args) {

        try (FileInputStream fio = new FileInputStream("C:\\Users\\JunjieChen\\Desktop\\测试\\寸照.png"))
        {

            // 1. 建立Socket连接
            Socket socket = new Socket("127.0.0.1", 8888);

            // 2. 数据输出流，进行socket的数据传输
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // 2.1 先服务端发送一个文件的后缀
            dos.writeUTF(".png");
            dos.flush();

            // 3. 文件的一个输入流，进行文件的读取

            // 4. 进行文件的传输
            byte[] buffer = new byte[1024];
            int len;
            while((len = fio.read(buffer)) > 0){
                // 向socket的缓冲区中写入数据
                dos.write(buffer, 0, len);
            }
            dos.flush();

            // 数据发送完毕后，必须通知服务器 socket关闭
            socket.shutdownOutput();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
