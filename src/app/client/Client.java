package app.client;


import app.Connection;
import app.ConsoleManager;
import app.Message;
import app.MessageType;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private volatile boolean isClientConnected;
    protected Connection connection;


    public static void main(String[] args) {
        new Client().run();
    }

    public void run() {
        SocketReceiverThread thread = getNewSocketReceiverThread();
        thread.setDaemon(true);
        thread.run();
        synchronized (this) {
            try {
                wait();//waiting for "connected" status
                if (isClientConnected) {
                    System.out.println("You were connected to server. Type \'exit#\' to leave the chat");
                    while (isClientConnected) {
                        String newTextMessage = ConsoleManager.readString();
                        if (newTextMessage.equals("exit#")) break;
                        if (shouldTakeTextFromConsole())
                            connection.writeMessage(new Message(MessageType.TEXT_MESSAGE, newTextMessage));
                    }

                    ConsoleManager.writeToConsole("You were disconnected");

                } else ConsoleManager.writeToConsole("Error during client connection");

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    public class SocketReceiverThread extends Thread {
        protected String userName;

        @Override
        public void run() {
            try {
                Socket socket = new Socket(getServerAddress(), getServerPort());
                connection = new Connection(socket);
                serverHandshake();
                clientMainLoop();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                changeConnectionStatus(false);
            }
        }

        public void serverHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                Message messageFromServer = connection.readMessage();
                if (messageFromServer.getMessageType() == MessageType.NAME_REQUEST) {
                    String name = getUserName();
                    userName = name;
                    connection.writeMessage(new Message(MessageType.USER_NAME, name));
                }
                if (messageFromServer.getMessageType() == MessageType.NAME_ACCEPTED) {
                    changeConnectionStatus(true);
                    return;
                } else throw new IOException("Unknown inappropriate message type");
            }
        }


        public void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message messageFromServer = connection.readMessage();

                if (messageFromServer.getMessageType() == null) throw new IOException("Unknown MessageType");
                switch (messageFromServer.getMessageType()) {
                    case USER_ADDED:
                        welcomeNewUser(messageFromServer);
                        break;
                    case USER_REMOVED:
                        informAboutRemovedUser(messageFromServer);
                        break;
                    case TEXT_MESSAGE:
                        processIncomingMessage(messageFromServer);
                        break;
                    default:
                        throw new IOException("Unknown messageType");

                }
            }


        }

        protected void welcomeNewUser(Message messageFromServer) {
            ConsoleManager.writeToConsole("New user was added " + messageFromServer.getMessageData());
        }

        protected void informAboutRemovedUser(Message messageFromServer) {
            ConsoleManager.writeToConsole(messageFromServer.getMessageData() + " left the chat");
        }

        protected void processIncomingMessage(Message messageFromServer) {
            ConsoleManager.writeToConsole(messageFromServer.getMessageData());
        }

        protected void changeConnectionStatus(boolean isClientConnected) {
            Client.this.isClientConnected = isClientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }
    }


    protected String getServerAddress() {
        ConsoleManager.writeToConsole("Hello, please provide server address");
        return ConsoleManager.readString();
    }

    protected int getServerPort() {
        ConsoleManager.writeToConsole("Please provide server port");
        return ConsoleManager.readInt();

    }

    protected String getUserName() {
        ConsoleManager.writeToConsole("Please provide your name");
        return ConsoleManager.readString();
    }

    protected boolean shouldTakeTextFromConsole() {
        return true;
    }

    protected SocketReceiverThread getNewSocketReceiverThread() {
        return new SocketReceiverThread();
    }


}
