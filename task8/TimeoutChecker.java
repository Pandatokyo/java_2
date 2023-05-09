package task8;

import java.util.HashMap;
import java.util.Map;

public class TimeoutChecker {
    private static final TimeoutChecker timeoutChecker = new TimeoutChecker();
    private final static long TIME_TO_KICK = 120000;
    private static Map<ClientHandler, Long> nonAuthorizedSockets = new HashMap<>();


    private TimeoutChecker() {
        new Thread(() -> {
            while (true) {
                nonAuthorizedSockets.forEach(((client, time) -> {
                    if ((System.currentTimeMillis() - time) > TIME_TO_KICK) {
                        client.sendMessage("The authorization time is out.");
                        client.closeSocket();
                    }
                }));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void set(ClientHandler client) {
        nonAuthorizedSockets.put(client, System.currentTimeMillis());
    }

    public static void unset(ClientHandler client) {
        nonAuthorizedSockets.remove(client);
    }
}
