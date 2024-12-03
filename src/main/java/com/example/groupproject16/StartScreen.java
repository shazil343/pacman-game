
package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartScreen extends Application {

    private Runnable startGameAction; // A callback for starting the game

    public void setStartGameAction(Runnable startGameAction) {
        this.startGameAction = startGameAction;
    }

    @Override
    public void start(Stage primaryStage) {
        // Load the logo image
        Image logo = new Image(getClass().getResource("/Images/pac-man-logo.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(400);
        logoView.setFitHeight(250);
        logoView.setPreserveRatio(true);
        logoView.setLayoutX((600 - logoView.getFitWidth()) / 2);
        logoView.setLayoutY(-40);

        // Importing the Pac-Man font
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 35);

        // Create a text field for username input
        TextField usernameField = new TextField();
        usernameField.setPromptText("ENTER USERNAME");
        usernameField.setMaxWidth(300);
        usernameField.setLayoutX((600 - 300) / 2);
        usernameField.setLayoutY(200);

        // Create a button to start the game
        Button startButton = new Button("START GAME");
        startButton.setFont(customFont);
        startButton.setStyle("-fx-background-color: #f5eb36; -fx-text-fill: black;");
        startButton.setPrefWidth(225);
        startButton.setPrefHeight(30);
        startButton.setLayoutX((600 - startButton.getPrefWidth()) / 2);
        startButton.setLayoutY(275);

        // Set action for the start button
        startButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                System.out.println("Please enter a username.");
            } else {
                System.out.println("Welcome, " + username + "! Starting game...");
                if (startGameAction != null) {
                    startGameAction.run(); // Trigger the transition to the Maze
                }
            }
        });

        // Create the layout
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(logoView, usernameField, startButton);

        // Create and set the scene
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("Pac-Man Start Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
