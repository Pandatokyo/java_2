package task8;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Server server;
    private String name;
    private Channel channel;
    private final Socket socket;

    public ClientHandler(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;

        try {
            channel = ChannelBase.of(socket);
            new Thread(() -> {
                auth();
                System.out.println(name + " handler waiting for new messages");
                while (socket.isConnected()) {
                    Message msg = channel.getMessage();
                    if (msg == null) continue;
                    switch (msg.getType()) {
                        case EXIT_COMMAND:
                            server.unsubscribe(this);
                            break;
                        case PRIVATE_MESSAGE:
                            sendPrivateMessage(msg.getBody());
                            break;
                        case BROADCAST_CHAT:
                            server.sendBroadcastMessage(name + " : " + msg.getBody());
                        default:
                            System.out.println("invalid message type");
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPrivateMessage(String text) {
        int firstSpaceIndex = text.indexOf(" ");
        final String nameTo = text.substring(0, firstSpaceIndex);
        final String message = text.substring(firstSpaceIndex).trim();
        if (server.isNameTaken(nameTo)) {
            server.sendPrivateMessage(name, nameTo, name + " -> " + nameTo + " : " + message);
        } else {
            sendMessage(nameTo + " is available");
        }
    }

    private void auth() {
        while (true) {
            TimeoutChecker.set(this);
            if (!channel.hasNextLine()) break;
            Message message = channel.getMessage();
            if (MessageType.AUTH_MESSAGE.equals(message.getType())) {
                String[] commands = message.getBody().split(" ");
                if (commands.length >= 2) {
                    String login = commands[0];
                    String password = commands[1];
                    String name = server.getAuthService().credentialsAuth(login, password);
                    if (name == null) {
                        String msg = "Invalid credentials";
                        System.out.println(msg);
                        channel.sendMessage(msg);
                    } else if (server.isNameTaken(name)) {
                        String msg = "This username already exists";
                        System.out.println(msg);
                        channel.sendMessage(msg);
                    } else {
                        this.name = name;
                        String msg = "Authorized";
                        TimeoutChecker.unset(this);
                        System.out.println(msg);
                        channel.sendMessage(msg);
                        server.subscribe(this);
                        break;
                    }
                }
            } else {
                channel.sendMessage("Invalid command!");
            }
        }
    }

    public void sendMessage(String msg) {
        channel.sendMessage(msg);
    }

    public String getName() {
        return name;
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
