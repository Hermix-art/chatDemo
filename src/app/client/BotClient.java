package app.client;

import app.ConsoleManager;
import app.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class BotClient extends Client {
    public static void main(String[] args) {
        new BotClient().run();
    }

    public class BotSocketReceiverThread extends SocketReceiverThread {
        @Override
        public void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage(String.format("Hello, I'm %s bot. I understand the list of commands:\n" +
                    "min\n" +
                    "hour\n" +
                    "day\n" +
                    "month\n" +
                    "year\n" +
                    "time\n", userName));
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(Message messageFromServer) {
            String message = messageFromServer.getMessageData();
            ConsoleManager.writeToConsole(message);

            if(message == null || !message.contains(":")) return; // create special message for user

            String userName = message.substring(0, message.indexOf(":"));
            String messageText = message.substring(message.indexOf(":") + 1).trim();


            LocalDateTime localDateTime = LocalDateTime.now();

            switch (messageText) {
                case "date":
                    sendTextMessage(localDateTime.format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy")));
                    break;
                case "min":
                    sendTextMessage(localDateTime.getMinute() + "");
                    break;
                case "hour":
                    sendTextMessage(localDateTime.getHour() + "");
                    break;
                case "day":
                    sendTextMessage(localDateTime.getDayOfMonth() + "");
                    break;
                case "month":
                    sendTextMessage(localDateTime.getMonth() + "");
                    break;
                case "year":
                    sendTextMessage(localDateTime.getYear() + "");
                    break;
                case "time":
                    sendTextMessage(localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                    break;
            }
        }
    }


    @Override
    protected SocketReceiverThread getNewSocketReceiverThread() {
        return new BotSocketReceiverThread();
    }

    @Override
    protected String getUserName() {
        return "Bot#" + new Random(1000).nextInt();
    }

    @Override
    protected boolean shouldTakeTextFromConsole() {
        return false;
    }
}
