package app;

import app.Connection;
import app.ConsoleManager;
import app.Message;
import app.MessageType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final Map<String, Connection> activeUsers = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Please provide port number");
        int port = ConsoleManager.readInt();
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("app.Server is running");

            while (true) {
                Socket socketForUser = serverSocket.accept();
                new SocketThread(socketForUser).start();
            }
        }finally {
            serverSocket.close();
        }
    }

    public static void broadcastMessage(Message message)  {
        for (String user : activeUsers.keySet()) {
            try {
                activeUsers.get(user).writeMessage(message);
            } catch (IOException e) {
                ConsoleManager.writeToConsole("app.Message was not delivered");
            }
        }
    }

    public static class SocketThread extends Thread {
        private Socket socket;

        public SocketThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ConsoleManager.writeToConsole("New connection " + socket.getRemoteSocketAddress());
            try (Connection connection = new Connection(socket)){

                String userName = newUserVerification(connection);
                notifyUser(userName, connection);
                broadcastMessage(new Message(MessageType.USER_ADDED, userName));

                mailLoop(connection, userName);

                activeUsers.remove(userName);
                broadcastMessage(new Message(MessageType.USER_REMOVED, userName));

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        private String newUserVerification(Connection connection) throws IOException, ClassNotFoundException {
                connection.writeMessage(new Message(MessageType.NAME_REQUEST, "Hello, please provide your name"));
                Message messageFromUser = connection.readMessage();

                if (messageFromUser.getMessageType() == MessageType.USER_NAME
                        && !messageFromUser.getMessageData().equals("")
                        && !activeUsers.containsKey(messageFromUser.getMessageData())) {

                    connection.writeMessage(new Message(MessageType.NAME_ACCEPTED));
                    String userName = messageFromUser.getMessageData();
                    activeUsers.put(userName, connection);
                    return userName;

                } else return newUserVerification(connection);

        }

        private void notifyUser(String userName, Connection connection) throws IOException {
            for (String user : activeUsers.keySet()) {
                if (!userName.equals(user)) {
                    Message message = new Message(MessageType.USER_ADDED, user);
                    connection.writeMessage(message);
                }
            }

        }

        private void mailLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.readMessage();
                if (message.getMessageType() == MessageType.TEXT_MESSAGE) {
                    String messageText = userName + ": " + message.getMessageData();
                    broadcastMessage(new Message(MessageType.TEXT_MESSAGE, messageText));
                } else ConsoleManager.writeToConsole("Error: the message is not text message");
            }

        }

    }


}
