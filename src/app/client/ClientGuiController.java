package app.client;

import app.Message;

public class ClientGuiController extends Client {
    private final ClientGuiModel model;
    private final ClientGuiView view;

    public static void main(String[] args) {
        ClientGuiController controller = new ClientGuiController();
        controller.run();
    }

    public ClientGuiController() {
        model = new ClientGuiModel();
        view = new ClientGuiView(this);
    }

    public ClientGuiModel getModel() {
        return model;
    }

    private class GuiSocketReceiverThread extends SocketReceiverThread {
        @Override
        protected void welcomeNewUser(Message messageFromServer) {
            getModel().addUser(messageFromServer.getMessageData());
            view.refreshUsers();
        }

        @Override
        protected void processIncomingMessage(Message messageFromServer) {
            getModel().setLastAvailableMessage(messageFromServer.getMessageData());
            view.refreshMessage();
        }

        @Override
        protected void informAboutRemovedUser(Message messageFromServer) {
            getModel().deleteUser(messageFromServer.getMessageData());
            view.refreshUsers();
        }

        @Override
        protected void changeConnectionStatus(boolean isClientConnected) {
            view.updateConnectionStatus(isClientConnected);
        }
    }

    @Override
    public void run() {
        SocketReceiverThread thread = getNewSocketReceiverThread();
        thread.run();
    }

    @Override
    protected String getServerAddress() {
        return view.getServerAddress();
    }

    @Override
    protected int getServerPort() {
        return view.getPort();
    }

    @Override
    protected String getUserName() {
        return view.getUserName();
    }

    @Override
    protected SocketReceiverThread getNewSocketReceiverThread() {
        return new GuiSocketReceiverThread();
    }
}
