package netty.c4.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chenjunjie
 * @version 1.0.0
 * @ClassName Message.java
 * @Description TODO
 * @createTime 2021年11月09日 16:42:00
 */
public abstract class Message implements Serializable {


//    public static class<?> getMessageClass(int messageType) { return messageClasses.get(messageType);

    private int sequenceId;
    private int messageType;

    public abstract int getMessageType();


    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMemberRequestMessage = 12;
    public static final int GroupMemberResponseMessage = 13;

    private static final Map<Integer, Class<?>> messageClasses = new HashMap<>();





}
