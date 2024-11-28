package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HumanPlayer extends Application {

    private static final int TILE_SIZE = 30; // Size of each grid tile
    private static final int ROWS = 20; // Number of rows in the maze
    private static final int COLUMNS = 20; // Number of columns in the maze

    private int playerRow = 1; // Starting position of Pac-Man (row)
    private int playerCol = 1; // Starting position of Pac-Man (column)
    private int lives = 3; // Player lives

    private Circle pacman; // Pac-Man's visual representation
    private Text livesText; // Text to display lives

    private final int[][] mazeLayout = {
            // Maze layout: 0 = path, 1 = wall
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    @Override
    public void start(Stage primaryStage) {
        // Create the grid for the maze
        GridPane mazeGrid = new GridPane();
        mazeGrid.setStyle("-fx-background-color: black;");

        // Draw the maze
        for (int row = 0; row < mazeLayout.length; row++) {
            for (int col = 0; col < mazeLayout[row].length; col++) {
                if (mazeLayout[row][col] == 1) {
                    mazeGrid.add(new Circle(TILE_SIZE / 2, Color.BLUE), col, row); // Wall
                }
            }
        }

        // Create Pac-Man
        pacman = new Circle(TILE_SIZE / 2 - 5, Color.YELLOW);
        mazeGrid.add(pacman, playerCol, playerRow);

        // Create lives text
        livesText = new Text("Lives: " + lives);
        livesText.setFill(Color.WHITE);
        livesText.setStyle("-fx-font-size: 20px;");
        mazeGrid.add(livesText, 0, ROWS);

        // Set up keyboard controls
        Scene scene = new Scene(mazeGrid, TILE_SIZE * COLUMNS, TILE_SIZE * ROWS);
        scene.setOnKeyPressed(this::handleMovement);

        // Configure the stage
        primaryStage.setTitle("Pac-Man: Human Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleMovement(KeyEvent event) {
        int newRow = playerRow;
        int newCol = playerCol;

        switch (event.getCode()) {
            case UP:
                newRow--;
                break;
            case DOWN:
                newRow++;
                break;
            case LEFT:
                newCol--;
                break;
            case RIGHT:
                newCol++;
                break;
            default:
                return; // Ignore other keys
        }

        // Check if the move is valid
        if (isMoveValid(newRow, newCol)) {
            playerRow = newRow;
            playerCol = newCol;
            GridPane.setRowIndex(pacman, playerRow);
            GridPane.setColumnIndex(pacman, playerCol);
        } else {
            // Simulate losing a life if Pac-Man hits a wall
            loseLife();
        }
    }

    private boolean isMoveValid(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLUMNS && mazeLayout[row][col] == 0;
    }

    private void loseLife() {
        lives--;
        livesText.setText("Lives: " + lives);

        if (lives <= 0) {
            System.out.println("Game Over!");
            System.exit(0); // Exit the game
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
