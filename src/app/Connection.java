package app;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class Connection implements Closeable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void writeMessage(Message message) throws IOException {
        synchronized (this){
            out.writeObject(message);
        }
    }
    public Message readMessage() throws IOException, ClassNotFoundException {
        Message message = (Message) in.readObject();
        return message;
    }

    public SocketAddress getRemoteAddress(){
        return socket.getRemoteSocketAddress();
    }

    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}
