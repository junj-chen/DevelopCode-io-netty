package nio.c1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @title: TestByteBufferExam
 * @Description // 演示使用 ByteBuffer的API进行 粘包 解包
 * @Author Chen
 * @Date: 2021/11/3 19:24
 * @Version 1.0
 */
public class TestByteBufferExam {

    public static void main(String[] args) {

        ByteBuffer source = ByteBuffer.allocate(32);
        // 字符串出现了 隔断，但是没有分隔开
        source.put("hello,word\nI'm zhangshan\nHo".getBytes(StandardCharsets.UTF_8));
        split(source);
        source.put("w are you?".getBytes(StandardCharsets.UTF_8));
        split(source);


    }

    private static void split(ByteBuffer source) {
        // 进行字符串的分割
        source.flip();   //切换为读模式

        for(int i = 0; i < source.limit(); i++){

            if(source.get(i) == '\n'){  // get(index) 不会移动 position
                // 出现隔断符
                int len = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(len);

                for(int j = 0; j < len; j++){
                    target.put(source.get());  // 将切断的字符串放入目标串中， get() 方法会移动 position

                }

                System.out.println(new String(target.array()));
            }
        }

        // 切换写模式，但是需要移动未读写的字符串
        source.compact();

    }

}
