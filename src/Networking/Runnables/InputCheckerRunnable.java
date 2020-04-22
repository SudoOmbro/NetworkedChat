package Networking.Runnables;

import Networking.Message;
import Networking.NetworkObject;

import java.util.Stack;

public class InputCheckerRunnable extends GeneralRunnable {

    private NetworkObject target;
    private Stack<Message> messageStack;

    public InputCheckerRunnable(NetworkObject target) {
        this.target = target;
        this.messageStack = target.getMessageStack();
    }

    @Override
    protected void runFunction() {
        //Checks if there are pending messages to handle from the socket
        if (!messageStack.empty()) {
            Message m = messageStack.pop();
            if (m != null) {
                target.handleMessage(m);
            }
        }
    }

}
