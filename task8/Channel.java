package task8;

public interface Channel {
    default void sendMessage(String msg) {
        sendMessage(new Message(MessageType.BROADCAST_CHAT, msg));
    }

    void sendMessage(Message message);

    Message getMessage();

    boolean hasNextLine();
}
