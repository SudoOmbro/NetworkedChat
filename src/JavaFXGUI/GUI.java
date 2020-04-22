package JavaFXGUI;

import Networking.Client;
import Networking.Message;
import Utils.ChatLines;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static Utils.Utils.say;

public class GUI extends Application {

    private Client client;
    private GUI self = this;

    private ChatLines chatLines = new ChatLines(30);
    private Text chatText;

    private Stage primaryStage;
    private Scene loginWindow;
    private Scene chatWindow;

    private Thread observer;

    Task observerTask = new Task<Void>() {
        @Override public Void call() {
            while (true) {
                if (isCancelled()) {
                    say("breaking...");
                    break;
                }
                if (client.newMessage) {
                    say("observed message");
                    client.newMessage = false;
                    ArrayList<Message> msgs = client.getMessageList();
                    Message msg = msgs.get(msgs.size() - 1);
                    msg.print();
                    update(msg);
                }
            }
            say("While ended");
            return null;
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);

        loginWindow = initLoginWindow();
        chatWindow = initChatWindow();

        setWindow("Login", loginWindow);
    }

    private Scene initLoginWindow() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label userNameLabel = new Label("User name:");
        userNameLabel.setTextFill(Color.WHITE);
        grid.add(userNameLabel, 0, 0);

        TextField userNameField = new TextField();
        grid.add(userNameField, 1, 0);

        Label serverLabel = new Label("Server Name:");
        serverLabel.setTextFill(Color.WHITE);
        grid.add(serverLabel, 0, 1);

        TextField serverField = new TextField();
        grid.add(serverField, 1, 1);

        Button okButton = new Button("Join");
        grid.add(okButton, 1, 2);

        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String username = userNameField.getText();
                String servername = serverField.getText();
                if (username.compareTo("") != 0 && servername.compareTo("") != 0) {
                    say("Ok");
                    try {
                        client = new Client(username, servername, 8000);
                        client.connectToServer();
                        setWindow("Chat", chatWindow);
                        observer = new Thread(observerTask);
                        observer.start();
                    } catch (UnknownHostException e) {
                        say("Unknown Host!");
                    } catch (IOException e) {
                        say("Can't connect to server!");
                    }
                }
            }
        });

        root.getChildren().add(grid);
        scene.setFill(Color.BLACK);
        return scene;
    }

    private Scene initChatWindow() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        TextField textField = new TextField();
        textField.setMinWidth(1000);
        Button sendButton = new Button("Send");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setAlignment(Pos.BOTTOM_LEFT);
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.add(textField, 0, 0);
        grid.add(sendButton, 1, 0);

        chatLines.add("Welcome!");
        chatText = new Text();
        chatText.setTranslateX(10);
        chatText.setTranslateY(10);
        chatText.setFill(Color.WHITE);
        chatText.setTextAlignment(TextAlignment.LEFT);
        chatText.setLayoutX(0);
        chatText.setLayoutY(0);

        Rectangle textRectangle = new Rectangle(1000, 600);
        textRectangle.setFill(Color.color(0.1, 0.1, 0.1));

        StackPane textPane = new StackPane();
        textPane.getChildren().addAll(textRectangle, chatText);
        textPane.setAlignment(Pos.TOP_LEFT);
        textPane.setTranslateX(10);
        textPane.setTranslateY(10);

        sendButton.setOnMouseClicked(event -> {
            String textToSend = textField.getText();
            textField.clear();
            if (textToSend.compareTo("") != 0) {
                Message msg = new Message(client.getUsername(), textToSend);
                client.sendMessage(msg);
            }
        });

        root.getChildren().add(textPane);
        root.getChildren().add(grid);
        scene.setFill(Color.BLACK);
        update(null);
        return scene;
    }

    private void setWindow(String windowName, Scene scene) {
        primaryStage.setTitle(windowName);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void update(Message message) {
        if (message != null)
            chatLines.add(message.getUsername() + ": " + message.getText());
        chatText.setText("");
        for (String s : chatLines.getStringList()) {
            String currentText = chatText.getText();
            chatText.setText(currentText + s + "\n");
        }
    }
}
