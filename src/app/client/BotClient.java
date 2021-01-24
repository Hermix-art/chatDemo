package app.client;

import app.ConsoleManager;
import app.Message;

import java.io.IOException;
import java.util.Random;

public class BotClient extends Client {
    public static void main(String[] args) {
        new BotClient().run();
    }

    public class BotSocketReceiverThread extends SocketReceiverThread {
        @Override
        public void clientMainLoop() throws IOException, ClassNotFoundException {
            ConsoleManager.writeToConsole(String.format("Hello, I'm %s bot. I understand the list of commands:\n" +
                    "min\n" +
                    "hour\n" +
                    "day\n" +
                    "month\n" +
                    "year\n" +
                    "time\n", userName));
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(Message messageFromServer) { // to be overridden
            super.processIncomingMessage(messageFromServer);
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
