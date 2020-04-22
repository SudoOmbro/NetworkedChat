package Networking;

import Networking.Runnables.InputCheckerRunnable;
import Networking.Runnables.LinstenerRunnable;
import Networking.Runnables.ServerRunnable;
import Utils.Utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends NetworkObject {

    private boolean open = false;

    private ServerSocket serverSocket;
    private ArrayList<Socket> connectionSockets;

    private Thread serverThread;
    private ArrayList<Thread> listenerThreads;

    private Thread inputCheckerThread;

    public Server(String serverName, int serverPort) {
        try {

            serverSocket = new ServerSocket(serverPort, 10, InetAddress.getByName(serverName));
            connectionSockets = new ArrayList<>();
            listenerThreads = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addConnection(Socket s) {
        connectionSockets.add(s);
        Thread lt = new Thread(new LinstenerRunnable(s, this));
        listenerThreads.add(lt);
        lt.start();
    }

    public void open() {
        if (!open) {
            serverThread = new Thread(new ServerRunnable(serverSocket, this));
            inputCheckerThread = new Thread(new InputCheckerRunnable(this));
            Utils.say("Server started on " + serverSocket.getInetAddress());
            serverThread.start();
            inputCheckerThread.start();
            open = true;
        }
        else
            System.err.println("Server is already opened!");
    }

    @Override
    public void close() throws IOException {
        if (open) {
            serverThread.interrupt();
            serverSocket.close();
            inputCheckerThread.interrupt();
            for (Thread t : listenerThreads)
                t.interrupt();
            for (Socket s : connectionSockets)
                s.close();
            open = false;
        }
        else
            System.err.println("Server is already closed!");
    }

    private void broadcastMessage(Message message) throws IOException {
        ObjectOutputStream out;
        OutputStream stream;
        for (Socket s : connectionSockets) {
            Utils.say("Sending message to: " + s.getInetAddress());
            GeneralNetwork.sendMessageToSocket(s, message);
        }
    }

    @Override
    public void handleMessage(Message message) {
        Utils.say("received message:", message.getUsername(), message.getText());
        try {
            broadcastMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleDisconnection(Socket s) {
        int index = connectionSockets.indexOf(s);
        connectionSockets.remove(index);
        listenerThreads.remove(index);
    }

}
