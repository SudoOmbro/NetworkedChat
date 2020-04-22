package Networking.Runnables;

import Networking.Message;
import Networking.NetworkObject;
import Utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class LinstenerRunnable extends GeneralRunnable {

    private Socket targetSocket;
    private NetworkObject networkObject;

    public LinstenerRunnable(Socket targetSocket, NetworkObject networkObject) {
        this.targetSocket = targetSocket;
        this.networkObject = networkObject;
    }

    @Override
    protected void runFunction() {
        /*
        listens for messages coming from the targetSocket, pushes them to the networked object
        message stack to be handled by the InputCheckerThread.
        Kills self if the connection is interrupted.
        */
        try {
            InputStream stream = targetSocket.getInputStream();
            if (stream != null) {
                ObjectInputStream objectStream = new ObjectInputStream(stream);
                Message m = (Message) objectStream.readObject();
                if (m != null) {
                    networkObject.getMessageStack().push(m);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Utils.say("Connection closed on socket: " + targetSocket.getInetAddress(), "Killing self...");
            networkObject.handleDisconnection(targetSocket);
            Thread.currentThread().interrupt();
        }
    }

}
