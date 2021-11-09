package netty.c3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName TestLengthFieldDecoder.java
 * @Description 测试 netty 的编解码实现
 * @createTime 2021年11月09日 10:59:00
 */
public class TestLengthFieldDecoder {

    public static void main(String[] args) {

        EmbeddedChannel channel = new EmbeddedChannel(



                new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 4),
                new LoggingHandler(LogLevel.DEBUG)
        );

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        make(buffer, "hello worrrrrr");
        make(buffer, "hekkkssss ccccccccc");

        channel.writeInbound(buffer);



    }

    public static void make(ByteBuf buffer, String str){

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;

        buffer.writeInt(length);
        buffer.writeByte(1);  // 添加版本
        buffer.writeBytes(bytes);

    }



}
