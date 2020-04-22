package Networking.Runnables;

import Utils.Utils;

public abstract  class GeneralRunnable implements Runnable {

    //class from which each runnable inherits in order to make "infinite" threads easier to make and handle.

    @Override
    public void run() {
        //run indefinitely until Interrupt signal.
        Utils.say(" Running");
        while (!Thread.currentThread().isInterrupted())
            runFunction();
    }

    //defines what the thread does while running
    protected abstract void runFunction();

}
