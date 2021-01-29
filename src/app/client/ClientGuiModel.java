package app.client;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClientGuiModel {
    private final Set<String> currentUsers = new HashSet<>();
    private String lastAvailableMessage;

    public Set<String> getCurrentUsers(){
        return Collections.unmodifiableSet(currentUsers);
    }

    public void deleteUser(String userName){
        currentUsers.remove(userName);
    }
    public void addUser(String userName){
        currentUsers.add(userName);
    }

    public String getLastAvailableMessage() {
        return lastAvailableMessage;
    }

    public void setLastAvailableMessage(String lastAvailableMessage) {
        this.lastAvailableMessage = lastAvailableMessage;
    }

}
