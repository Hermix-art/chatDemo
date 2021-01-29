package app.client;

public class ClientGuiController extends Client {
    private final ClientGuiModel model;

    public ClientGuiController(ClientGuiModel model) {
        this.model = model;
    }

    public ClientGuiModel getModel() {
        return model;
    }
}
