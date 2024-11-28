package com.example.groupproject16;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Title
        Text title = new Text("Pac-Man Character Selection");
        title.setFont(Font.font("Arial", 30));
        title.setFill(Color.YELLOW);

        // Username input
        Text usernameLabel = new Text("Enter Your Username:");
        usernameLabel.setFont(Font.font("Arial", 18));
        usernameLabel.setFill(Color.WHITE);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        // Character selection
        Text characterLabel = new Text("Select Your Character:");
        characterLabel.setFont(Font.font("Arial", 18));
        characterLabel.setFill(Color.WHITE);

        ChoiceBox<String> characterChoiceBox = new ChoiceBox<>();
        characterChoiceBox.getItems().addAll("Classic Pac-Man", "Speedy Pac-Man", "Stealth Pac-Man", "Power Pac-Man");
        characterChoiceBox.setValue("Classic Pac-Man");

        // Start button
        Button startButton = new Button("Start Game");
        startButton.setFont(Font.font("Arial", 18));
        startButton.setStyle("-fx-background-color: #00ff00; -fx-text-fill: black; -fx-border-color: #009900; -fx-border-width: 2px;");
        startButton.setOnAction(e -> {
            String username = usernameField.getText();
            String selectedCharacter = characterChoiceBox.getValue();

            if (username.isEmpty()) {
                System.out.println("Please enter a username.");
            } else {
                System.out.println("Username: " + username);
                System.out.println("Selected Character: " + selectedCharacter);
                // Transition to the game screen (not implemented here)
            }
        });

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(20);
        gridPane.add(title, 0, 0, 2, 1);
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(characterLabel, 0, 2);
        gridPane.add(characterChoiceBox, 1, 2);
        gridPane.add(startButton, 0, 3, 2, 1);

        gridPane.setStyle("-fx-background-color: black; -fx-padding: 20;");

        // Scene and Stage
        Scene scene = new Scene(gridPane, 600, 400);
        primaryStage.setTitle("Pac-Man Character Selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
