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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Maze extends Application {

    private static final int TILE_SIZE = 10;
    private static final int ROWS = 72;
    private static final int COLUMNS = 55;
    private String[][] mazeLayout;

    private ImageView pacMan;
    private String currentDirection = null;
    private Timeline movementTimeline;

    private int pacManRow;
    private int pacManCol;

    private int currentScore = 54;
    private int currentLevel = 0;

    @Override
    public void start(Stage primaryStage) {
        // Load the maze from the file
        mazeLayout = loadMazeFromFile("src/resources/PacManMap.txt");

        // Find Pac-Man's starting position ('P')
        findStartPosition();

        // Create the maze grid
        GridPane mazeGrid = new GridPane();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                char tileType = mazeLayout[row][col] != null && !mazeLayout[row][col].isEmpty()
                        ? mazeLayout[row][col].charAt(0)
                        : 'E';

                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                StackPane cell = new StackPane();

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

        mazeGrid.setLayoutY(50);

        // Create Pac-Man
        pacMan = createPacMan();
        pacMan.setX(pacManCol * TILE_SIZE);
        pacMan.setY(pacManRow * TILE_SIZE);

        // Create a Pane to hold the maze and Pac-Man
        Pane root = new Pane();
        root.getChildren().addAll(mazeGrid, pacMan);

        // Add level and score text
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 25);
        Text level = new Text("LEVEL: " + currentLevel);
        level.setFont(customFont);
        level.setFill(Color.WHITE);
        level.setX(10);
        level.setY(30);

        Text score = new Text("SCORE: " + currentScore);
        score.setFont(customFont);
        score.setFill(Color.WHITE);
        score.setX(350);
        score.setY(30);

        root.getChildren().addAll(level, score);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, TILE_SIZE * COLUMNS, TILE_SIZE * ROWS + 50, Color.BLACK);
        scene.setOnKeyPressed(this::handleKeyPress);

        // Continuous movement timeline
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> movePacMan()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();

        primaryStage.setTitle("Pac-Man Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void findStartPosition() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (mazeLayout[row][col] != null && !mazeLayout[row][col].isEmpty() && mazeLayout[row][col].equals("P")) {
                    pacManRow = row;
                    pacManCol = col;
                    return;
                }
            }
        }
        // Default position if 'P' is not found
        pacManRow = ROWS / 2;
        pacManCol = COLUMNS / 2;
        System.out.println("Pac-Man starting position not found. Defaulting to center.");
    }

    private ImageView createPacMan() {
        Image pacManImage = new Image(getClass().getResource("/Images/moving-pacman.gif").toExternalForm());
        ImageView pacMan = new ImageView(pacManImage);
        pacMan.setFitWidth(TILE_SIZE * 2.5); // Larger than tiles
        pacMan.setFitHeight(TILE_SIZE * 2.5); // Larger than tiles
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

        // Allow movement only on 'S', 'P', or 'N'
        char tileType = mazeLayout[row][col] != null && !mazeLayout[row][col].isEmpty()
                ? mazeLayout[row][col].charAt(0)
                : 'E';
        return tileType == 'S' || tileType == 'P' || tileType == 'N';
    }

    private String[][] loadMazeFromFile(String fileName) {
        String[][] maze = new String[ROWS][COLUMNS];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < ROWS) {
                String[] rowElements = line.trim().split("\\s+"); // Split by whitespace
                for (int col = 0; col < COLUMNS && col < rowElements.length; col++) {
                    maze[row][col] = rowElements[col].isEmpty() ? "E" : rowElements[col];
                }
                row++;
            }
        } catch (IOException e) {
            System.out.println("Error reading the maze file: " + e.getMessage());
            // Default to an empty maze
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    maze[row][col] = "E";
                }
            }
        }
        return maze; //
    }

    public static void main(String[] args) {
        launch(args);
    }
}

