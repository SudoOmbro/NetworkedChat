package Networking;

import java.io.IOException;
import java.net.Socket;
import java.util.Stack;

public abstract class NetworkObject {

    //Client AND Server have a message stack to store incoming messages
    protected Stack<Message> messageStack;

    public NetworkObject() {
        this.messageStack = new Stack<>();
    }

    //this function can handle logic on incoming payloads.
    public abstract void handleMessage(Message message);

    //this function handles client/server disconnections.
    public abstract void handleDisconnection(Socket s);

    //this functions handles the killing of the client/server
    public abstract void close() throws IOException;

    public Stack<Message> getMessageStack() {
        return messageStack;
    }

}
