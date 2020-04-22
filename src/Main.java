import Networking.Client;
import Networking.Message;
import Networking.Server;
import Utils.Utils;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    //TEST CLASS

    public static void main(String[] args) throws IOException {
        Utils.say("0 = server", "1 = client");
        Scanner scanner = new Scanner(System.in);
        int dec = Integer.parseInt(args[0]);
        if (dec == 0) {
            Server server = new Server(null, 8000);
            server.open();
        }
        else {
            Client client = new Client("AAA", null, 8000);
            client.connectToServer();
            Message msg = new Message("Ombro", "Ciao");
            Utils.say("Trying to send message...");
            client.sendMessage(msg);
            client.close();
        }
    }

}
