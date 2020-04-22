package Networking;

import Networking.Runnables.InputCheckerRunnable;
import Networking.Runnables.LinstenerRunnable;
import Utils.Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client extends NetworkObject{

    private Thread listenerThread;
    private Thread inputCheckerThread;

    private Socket socket;
    private InetAddress serverIP;
    private int serverPort;

    public boolean newMessage = false;
    private ArrayList<Message> messageList;

    private String username;

    public String getUsername() {
        return username;
    }

    public Client(String username, String serverName, int serverPort) throws UnknownHostException {
        super();
        this.username = username;
        this.serverPort = serverPort;
        serverIP = InetAddress.getByName(serverName);
        messageList = new ArrayList<>();
    }

    public void connectToServer() throws IOException {
        this.serverIP = serverIP;
        socket = new Socket(serverIP, serverPort);
        Utils.say("Connected to server with IP " + socket.getInetAddress());

        listenerThread = new Thread(new LinstenerRunnable(socket, this));
        inputCheckerThread = new Thread(new InputCheckerRunnable(this));

        listenerThread.start();
        inputCheckerThread.start();

        Utils.say("Threads started");
    }

    @Override
    public void handleMessage(Message message) {
        Utils.say("received message:", message.getUsername(), message.getText());
        messageList.add(message);
        newMessage = true;
    }

    @Override
    public void handleDisconnection(Socket s) {
        inputCheckerThread.interrupt();
    }

    @Override
    public void close() throws IOException {
        if (!inputCheckerThread.isInterrupted())
            inputCheckerThread.interrupt();
        if (!listenerThread.isInterrupted())
            listenerThread.interrupt();
        Utils.say("Client process closed.");
    }

    public void sendMessage(Message message) {
        try {
            GeneralNetwork.sendMessageToSocket(socket, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Message> getMessageList() {
        return messageList;
    }

}
