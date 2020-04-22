package JavaFXGUI;

import Networking.Client;
import Networking.Message;

import java.util.ArrayList;

import static Utils.Utils.say;

public class ObserverRunnable implements Runnable{

    private Client client;
    private GUI gui;

    public ObserverRunnable(Client client, GUI gui) {
        this.client = client;
        this.gui = gui;
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                say("killing self...");
                return;
            }
            if (client.newMessage) {
                say("observed message");
                client.newMessage = false;
                ArrayList<Message> msgs = client.getMessageList();
                Message msg = msgs.get(msgs.size() - 1);
                msg.print();
                gui.update(msg);
            }
        }
    }

}
