package Networking;

import Utils.Utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GeneralNetwork {

    public static void sendMessageToSocket(Socket s, Message message) throws IOException {
        //general function to send a message to a connected socket
        Utils.say("Sending message:", message.getUsername(), message.getText());
        OutputStream stream = s.getOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(stream);
        out.writeObject(message);
    }

}
