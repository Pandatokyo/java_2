package task8;

import java.util.Objects;

public class Message {

    private MessageType type;
    private String body;

    public Message(MessageType type, String msg) {
        this.type = type;
        this.body = msg;
    }

    public static Message authMsg(String msg) {
        return new Message(MessageType.AUTH_MESSAGE, msg);
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
