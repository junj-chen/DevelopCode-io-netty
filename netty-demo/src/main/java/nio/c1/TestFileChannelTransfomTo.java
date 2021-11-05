package nio.c1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @title: TestFileChannelTransfomTo
 * @Description //TODO
 * @Author Chen
 * @Date: 2021/11/3 20:53
 * @Version 1.0
 */
public class TestFileChannelTransfomTo {

    public static void main(String[] args) {

        try (
                FileChannel form = new FileInputStream("netty-demo/target/data.txt").getChannel();
                FileChannel to = new FileOutputStream("netty-demo/target/to.txt").getChannel()
        ) {

            form.transferTo(0, form.size(), to);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
