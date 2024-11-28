package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PacManGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a title
        Text title = new Text("Pac-Man");
        title.setFont(Font.font("Arial", 60));
        title.setFill(Color.YELLOW);

        // Create a subtitle or instruction
        Text subtitle = new Text("Press Start to Play");
        subtitle.setFont(Font.font("Arial", 20));
        subtitle.setFill(Color.WHITE);

        // Create a start button
        Button startButton = new Button("Start Game");
        startButton.setFont(Font.font("Arial", 18));
        startButton.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black; -fx-border-color: #ff9900; -fx-border-width: 2px;");

        // Set action for the start button
        startButton.setOnAction(e -> {
            // Transition to the game scene (not implemented here)
            System.out.println("Game Starting...");
        });

        // Layout for the screen
        VBox root = new VBox(20);
        root.getChildren().addAll(title, subtitle, startButton);
        root.setStyle("-fx-background-color: black; -fx-alignment: center; -fx-padding: 20;");

        // Create the scene
        Scene scene = new Scene(root, 600, 400);

        // Configure the stage
        primaryStage.setTitle("Pac-Man");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
