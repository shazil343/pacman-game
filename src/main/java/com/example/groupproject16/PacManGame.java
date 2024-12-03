package com.example.groupproject16;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PacManGame extends Application {

    private static final int TILE_SIZE = 10;
    private static final int ROWS = 72;
    private static final int COLUMNS = 55;

    private String[][] mazeLayout;
    private ImageView pacMan;
    private String currentDirection = null; // Current movement direction
    private Timeline movementTimeline;
    private String username = ""; // Store username from StartScreen

    private int pacManRow;
    private int pacManCol;

    @Override
    public void start(Stage primaryStage) {
        // Show the StartScreen first
        showStartScreen(primaryStage);
    }

    private void showStartScreen(Stage primaryStage) {
        // Create the StartScreen layout
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        // Load the logo image
        Image logo = new Image(getClass().getResource("/Images/pac-man-logo.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(400);
        logoView.setFitHeight(250);
        logoView.setPreserveRatio(true);
        logoView.setLayoutX((600 - logoView.getFitWidth()) / 2);
        logoView.setLayoutY(-40);

        // Create a text field for username input
        javafx.scene.control.TextField usernameField = new javafx.scene.control.TextField();
        usernameField.setPromptText("ENTER USERNAME");
        usernameField.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 25));
        usernameField.setMaxWidth(300);
        usernameField.setLayoutX((600 - 300) / 2);
        usernameField.setLayoutY(200);

        // Create a button to start the game
        javafx.scene.control.Button startButton = new javafx.scene.control.Button("START GAME");
        startButton.setFont(Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 25));
        startButton.setStyle("-fx-background-color: #f5eb36; -fx-text-fill: black;");
        startButton.setPrefWidth(225);
        startButton.setPrefHeight(30);
        startButton.setLayoutX((600 - startButton.getPrefWidth()) / 2);
        startButton.setLayoutY(275);

        // Set action for the start button
        startButton.setOnAction(event -> {
            String enteredUsername = usernameField.getText().trim();
            if (enteredUsername.isEmpty()) {
                System.out.println("Please enter a username.");
            } else {
                username = enteredUsername;
                System.out.println("Welcome, " + username + "! Starting game...");
                showMazeAndPacMan(primaryStage); // Transition to the maze and Pac-Man game
            }
        });

        // Add elements to the root pane
        root.getChildren().addAll(logoView, usernameField, startButton);

        // Create and set the scene
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("Pac-Man Start Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMazeAndPacMan(Stage primaryStage) {
        // Load the maze layout
        mazeLayout = loadMazeFromFile("src/resources/PacManMap.txt");

        // Find the starting position (1) in the maze
        findStartPosition();

        // Create the maze grid
        GridPane mazeGrid = new GridPane();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                char tileType = mazeLayout[row][col] != null ? mazeLayout[row][col].charAt(0) : 'E';

                StackPane cell = new StackPane();
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);

                switch (tileType) {
                    case 'W': // Wall
                        tile.setFill(Color.BLUE);
                        break;
                    case 'E': // Empty Space
                        tile.setFill(Color.BLACK);
                        break;
                    case 'S': // Small Dot
                        tile.setFill(Color.BLACK);
                        Circle dot = new Circle(TILE_SIZE / 4, Color.YELLOW);
                        cell.getChildren().add(dot);
                        break;
                }
                cell.getChildren().add(tile);
                mazeGrid.add(cell, col, row);
            }
        }

        // Create Pac-Man
        pacMan = createPacMan();
        pacMan.setX(pacManCol * TILE_SIZE);
        pacMan.setY(pacManRow * TILE_SIZE);

        // Create a Pane to hold the maze and Pac-Man
        Pane root = new Pane();
        root.getChildren().addAll(mazeGrid, pacMan);

        // Scene setup
        int sceneWidth = TILE_SIZE * COLUMNS;
        int sceneHeight = TILE_SIZE * ROWS + 50;
        Scene scene = new Scene(root, sceneWidth, sceneHeight, Color.BLACK);

        // Add key events for movement
        scene.setOnKeyPressed(this::handleKeyPress);

        // Continuous movement timeline
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> movePacMan()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();

        // Set up the stage
        primaryStage.setTitle("Pac-Man Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void findStartPosition() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (mazeLayout[row][col] != null && mazeLayout[row][col].equals("1")) {
                    pacManRow = row;
                    pacManCol = col;
                    return;
                }
            }
        }
    }

    private ImageView createPacMan() {
        Image pacManImage = new Image(getClass().getResource("/Images/moving-pacman.gif").toExternalForm());
        ImageView pacMan = new ImageView(pacManImage);
        pacMan.setFitWidth(TILE_SIZE * 2); // Larger than tiles
        pacMan.setFitHeight(TILE_SIZE * 2); // Larger than tiles
        return pacMan;
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP, W -> {
                currentDirection = "UP";
                pacMan.setRotate(270);
            }
            case DOWN, S -> {
                currentDirection = "DOWN";
                pacMan.setRotate(90);
            }
            case LEFT, A -> {
                currentDirection = "LEFT";
                pacMan.setRotate(180);
            }
            case RIGHT, D -> {
                currentDirection = "RIGHT";
                pacMan.setRotate(0);
            }
        }
    }

    private void movePacMan() {
        if (currentDirection == null) return;

        int nextRow = pacManRow;
        int nextCol = pacManCol;

        switch (currentDirection) {
            case "UP" -> nextRow--;
            case "DOWN" -> nextRow++;
            case "LEFT" -> nextCol--;
            case "RIGHT" -> nextCol++;
        }

        // Check if the next position is valid
        if (isValidMove(nextRow, nextCol)) {
            pacManRow = nextRow;
            pacManCol = nextCol;
            pacMan.setX(pacManCol * TILE_SIZE);
            pacMan.setY(pacManRow * TILE_SIZE);
        }
    }

    private boolean isValidMove(int row, int col) {
        // Ensure within bounds
        if (row < 0 || col < 0 || row >= ROWS || col >= COLUMNS) {
            return false;
        }

        // Allow movement only on 'S'
        char tileType = mazeLayout[row][col].charAt(0);
        return tileType == 'S';
    }

    private String[][] loadMazeFromFile(String fileName) {
        String[][] maze = new String[ROWS][COLUMNS];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < ROWS) {
                String[] rowElements = line.trim().split(" ");
                for (int col = 0; col < COLUMNS && col < rowElements.length; col++) {
                    maze[row][col] = rowElements[col];
                }
                row++;
            }
        } catch (IOException e) {
            System.out.println("Error reading the maze file: " + e.getMessage());
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) maze[row][col] = "E"; // Default to empty space
            }
        }
        return maze;}}






/*
package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PacManGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create an instance of StartScreen
        StartScreen startScreen = new StartScreen();

        // Launch the start screen
        startScreen.start(primaryStage);

        // Add logic to handle game-over or other transitions if needed
    }

    public static void main(String[] args) {
        launch(args);
    }
}

 */