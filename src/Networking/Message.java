package Networking;

import Utils.Utils;

import java.io.Serializable;

import static Utils.Utils.say;

public class Message implements Serializable {

    private String username;
    private String text;

    public Message(String username, String text) {
        this.username = username;
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public void print() {
        say(username + ": " + text);
    }

}
