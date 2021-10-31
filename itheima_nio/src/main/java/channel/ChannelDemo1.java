package channel;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @title:
 * @Description // 演示 NIO 中的Channel API使用
 * @Author Chen
 * @Date: 2021/10/31 13:28
 * @Version 1.0
 */
public class ChannelDemo1 {


    @Test
    public void write(){
        /**
         * 演示fileChannel，写数据到指定的文件中
         */

        try {
            // 1. 文件输出流
            FileOutputStream fos = new FileOutputStream("fileTest01.txt");
            // 2. 文件channel
            FileChannel channel = fos.getChannel();

            // 3. 申请一个缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            String msg = "2021年10月31日13:33:01 学习 NIO";
            // 4. 写入数据到 buffer
            buffer.put(msg.getBytes(StandardCharsets.UTF_8));
            // 5. buffer切换为读模式
            buffer.flip();
            // 6. 将buffer中的数据写入到channel中
            channel.write(buffer);

            channel.close();

            System.out.println("文件写入成功");



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    @Test
    public void read() throws IOException {
        FileInputStream fis = new FileInputStream("fileTest01.txt");

        FileChannel channel = fis.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 数据读取到缓冲区
        channel.read(buffer);

        buffer.flip();

        String res = new String(buffer.array(), 0, buffer.remaining());

        System.out.println(res);

    }


    @Test
    public void copy() throws IOException {
        /**
         * 实现文件的 copy
         */

        File srcFile = new File("C:\\Users\\JunjieChen\\Desktop\\寸照.png");
        File destFile = new File("寸照_new.png");

        // 定义文件输入流读取文件
        FileInputStream fis = new FileInputStream(srcFile);
        // 定义文件的输出流，指定保存的文件位置
        FileOutputStream fos = new FileOutputStream(destFile);

        // 获取指定的 channel进行文件的读写
        FileChannel isChannel = fis.getChannel();
        FileChannel osChannel = fos.getChannel();

        // 声明缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (isChannel.read(buffer) != -1){
            // 切换模式
            buffer.flip();
            // 数据从buffer 中写入到 channel
            osChannel.write(buffer);

            buffer.clear();  // 清除标记，继续读写文件

        }

        // 关闭资源
        isChannel.close();
        osChannel.close();


    }

}
