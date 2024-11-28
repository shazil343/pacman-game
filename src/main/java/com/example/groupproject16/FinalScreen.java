package com.example.groupproject16;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FinalScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Simulated final score and leaderboard data
        int finalScore = 12345; // Example final score
        List<String> leaderboard = new ArrayList<>();
        leaderboard.add("1. Alice - 15000");
        leaderboard.add("2. Bob - 13000");
        leaderboard.add("3. You - " + finalScore);
        leaderboard.add("4. Carol - 12000");
        leaderboard.add("5. Dave - 10000");

        // Title
        Text title = new Text("Game Over");
        title.setFont(Font.font("Arial", 50));
        title.setFill(Color.RED);

        // Final score
        Text scoreText = new Text("Your Final Score: " + finalScore);
        scoreText.setFont(Font.font("Arial", 20));
        scoreText.setFill(Color.WHITE);

        // Leaderboard title
        Text leaderboardTitle = new Text("Leaderboard");
        leaderboardTitle.setFont(Font.font("Arial", 30));
        leaderboardTitle.setFill(Color.YELLOW);

        // Leaderboard list
        ListView<String> leaderboardView = new ListView<>();
        leaderboardView.getItems().addAll(leaderboard);
        leaderboardView.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        leaderboardView.setPrefHeight(150);

        // Play Again button
        Button playAgainButton = new Button("Play Again");
        playAgainButton.setFont(Font.font("Arial", 18));
        playAgainButton.setStyle("-fx-background-color: #00ff00; -fx-text-fill: black; -fx-border-color: #009900; -fx-border-width: 2px;");
        playAgainButton.setOnAction(e -> {
            System.out.println("Restarting game...");
            // Add logic to restart the game
        });

        // Exit button
        Button exitButton = new Button("Exit");
        exitButton.setFont(Font.font("Arial", 18));
        exitButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-border-color: #990000; -fx-border-width: 2px;");
        exitButton.setOnAction(e -> {
            System.out.println("Exiting game...");
            primaryStage.close();
        });

        // Layout
        VBox centerBox = new VBox(20, scoreText, leaderboardTitle, leaderboardView);
        centerBox.setStyle("-fx-alignment: center;");

        VBox buttonBox = new VBox(10, playAgainButton, exitButton);
        buttonBox.setStyle("-fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setTop(title);
        root.setCenter(centerBox);
        root.setBottom(buttonBox);

        BorderPane.setAlignment(title, javafx.geometry.Pos.CENTER);
        BorderPane.setAlignment(buttonBox, javafx.geometry.Pos.CENTER);

        root.setStyle("-fx-background-color: black; -fx-padding: 20;");

        // Scene and Stage
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Pac-Man - Final Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
