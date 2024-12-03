
package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class StartScreen extends Application {

    // Class-level variable to store the username
    private String username;

    public void start(Stage primaryStage) {
        // Load the image
        Image logo = new Image("pac-man-logo.png"); // Ensure the pac-man-logo.png file is in the resources folder or same directory

        // Create an ImageView to display the image
        ImageView logoView = new ImageView(logo);

        // Optional: Set size for the image
        logoView.setFitWidth(400); // Adjust width
        logoView.setFitHeight(250); // Adjust height
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
        usernameField.setFont(customFont);

        // Create a button to start the game
        Button startButton = new Button("START GAME");
        startButton.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 25));
        startButton.setStyle("-fx-background-color: #f5eb36; -fx-text-fill: black;");
        startButton.setPrefWidth(225);
        startButton.setPrefHeight(30);
        startButton.setLayoutX((600 - startButton.getPrefWidth()) / 2);
        startButton.setLayoutY(275);

        // Set action for the start button
        startButton.setOnAction(event -> {
            String enteredUsername = usernameField.getText().trim(); // Get and trim the text
            if (enteredUsername.isEmpty()) {
                System.out.println("Please enter a username.");
            } else {
                username = enteredUsername; // Store the username in the class-level variable
                System.out.println("Welcome, " + username + "! Starting game...");
                // Add logic to transition to the next scene or start the game
                primaryStage.close();
            }
        });

        // Create a vertical layout and add elements
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(logoView, usernameField, startButton);

        // Create a scene and set it on the stage
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("Pac-Man Start Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
