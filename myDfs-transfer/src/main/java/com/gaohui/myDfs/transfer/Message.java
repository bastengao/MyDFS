package com.gaohui.myDfs.transfer;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.List;

/**
 * 节点间传输消息的对象
 * User: Administrator
 * Date: 11-3-3 Time: 下午6:48
 *
 * @author Basten Gao
 */
public class Message implements Serializable {
    private MessageType messageType;
    private OperationType operationType;
    private Object value;

    public static Message createOkMessage() {
        Message message = new Message();
        message.setMessageType(MessageType.OK);
        return message;
    }

    public static Message createNoMessage() {
        Message message = new Message();
        message.setMessageType(MessageType.NO);
        return message;
    }

    public static Message createReadyMessage() {
        Message message = new Message();
        message.setMessageType(MessageType.READY);
        return message;
    }

    public static Message createBuildStreamLineMessage(List<SocketAddress> nextNodes) {
        Message message = new Message();
        message.setMessageType(Message.MessageType.BUILD_STREAM_LINE);
        message.setValue(nextNodes);
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public static enum OperationType {
        WRITE, READ;
    }

    public static enum MessageType {
        BUILD_STREAM_LINE, READY, OK, NO;
    }

    @Override
    public String toString() {
        return "Message{"
                + "messageType="
                + messageType
                + ", operationType="
                + operationType
                + ", value=" + value
                + '}';
    }
}
