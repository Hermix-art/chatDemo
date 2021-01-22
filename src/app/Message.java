package app;

import java.io.Serializable;

public class Message implements Serializable {
    private MessageType messageType;
    private String messageData;

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public Message(MessageType messageType, String messageData) {
        this.messageType = messageType;
        this.messageData = messageData;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessageData() {
        return messageData;
    }
}
