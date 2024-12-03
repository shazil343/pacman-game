package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FinalScreen extends Application {

    public void start(Stage primaryStage) {

        double scenceWidth = 600;
        double scenceHeight = 700;
        // Simulated final score and leaderboard data
        int finalScore = 12345; // Example final score
        List<String> leaderboard = new ArrayList<>();
        leaderboard.add("1. Alice - 15000");
        leaderboard.add("2. Bob - 13000");
        leaderboard.add("3. You - " + finalScore);
        leaderboard.add("4. Carol - 12000");
        leaderboard.add("5. Dave - 10000");

        // GAME OVER text implementation
        Text title = new Text("Game Over");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 60));
        title.setFill(Color.WHITE);
        title.setX((600 - title.getLayoutBounds().getWidth()) / 2);
        title.setY(50);

        // Final score
        Text scoreText = new Text("Your Final Score: " + finalScore);
        scoreText.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 25));
        scoreText.setFill(Color.YELLOW);
        scoreText.setX((600 -scoreText.getLayoutBounds().getWidth())/ 2);
        scoreText.setY(100);

        // Leaderboard title
        Text leaderboardTitle = new Text("Leaderboard");
        leaderboardTitle.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 50));
        leaderboardTitle.setFill(Color.YELLOW);
        leaderboardTitle.setX(100);
        leaderboardTitle.setY(160);

        // Leaderboard list
        ListView<String> leaderboardView = new ListView<>();
        leaderboardView.getItems().addAll(leaderboard);
        leaderboardView.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        leaderboardView.setPrefWidth(400);
        leaderboardView.setPrefHeight(150);
        leaderboardView.setLayoutX(100);
        leaderboardView.setLayoutY(320);

        // Play Again button
        Button playAgainButton = new Button("PLAY AGAIN");
        playAgainButton.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 30));
        playAgainButton.setStyle("-fx-background-color: #f5eb36; -fx-text-fill: white; -fx-border-width: 2px;");


        playAgainButton.setLayoutX((600 -scoreText.getLayoutBounds().getWidth())/ 2);
        playAgainButton.setLayoutY(500);
        playAgainButton.setOnAction(e -> {
            System.out.println("Restarting game...");
            // Add logic to restart the game
        });

        // Exit button
        Button exitButton = new Button("EXIT");
        exitButton.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 30));
        exitButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: black; -fx-border-color: #990000; -fx-border-width: 2px;");
        exitButton.setLayoutX(300);
        exitButton.setLayoutY(500);
        exitButton.setOnAction(e -> {
            System.out.println("Exiting game...");
            primaryStage.close();
        });

        // Pane for absolute positioning
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(title, scoreText, leaderboardTitle, leaderboardView, playAgainButton, exitButton);

        // Scene and Stage
        Scene scene = new Scene(root, scenceWidth, scenceHeight);
        primaryStage.setTitle("Pac-Man - Final Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

