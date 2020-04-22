package Networking.Runnables;

import Networking.Server;
import Utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunnable extends GeneralRunnable {

    private ServerSocket serverSocket;
    private Server serverObject;

    public ServerRunnable(ServerSocket serverSocket, Server serverObject) {
        this.serverSocket = serverSocket;
        this.serverObject = serverObject;
    }

    @Override
    protected void runFunction() {
        try {
            Socket pendingSocket = serverSocket.accept();
            if (pendingSocket != null) {
                Utils.say("New connection on socket " + pendingSocket.getInetAddress());
                serverObject.addConnection(pendingSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
