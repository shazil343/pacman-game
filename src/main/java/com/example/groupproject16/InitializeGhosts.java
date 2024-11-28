package com.example.groupproject16;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class InitializeGhosts extends Application {

    private static final int TILE_SIZE = 30; // Size of each grid tile
    private static final int ROWS = 10; // Number of rows in the maze
    private static final int COLUMNS = 10; // Number of columns in the maze
    private static final int NUM_GHOSTS = 4; // Number of ghosts

    private Circle[] ghosts = new Circle[NUM_GHOSTS]; // Array to hold ghosts
    private Timeline timeline; // Timeline for ghost movement
    private int level = 1; // Current level
    private Random random = new Random();

    private final int[][] mazeLayout = {
            // Maze layout: 0 = path, 1 = wall
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    @Override
    public void start(Stage primaryStage) {
        // Create the grid for the maze
        GridPane mazeGrid = new GridPane();

        // Draw the maze
        for (int row = 0; row < mazeLayout.length; row++) {
            for (int col = 0; col < mazeLayout[row].length; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                if (mazeLayout[row][col] == 1) {
                    tile.setFill(Color.BLUE); // Wall
                } else {
                    tile.setFill(Color.BLACK); // Path
                }
                mazeGrid.add(tile, col, row);
            }
        }

        // Initialize ghosts
        for (int i = 0; i < NUM_GHOSTS; i++) {
            Circle ghost = new Circle(TILE_SIZE / 2 - 5, getRandomGhostColor());
            ghosts[i] = ghost;

            // Place ghost at a random path tile
            int startRow = random.nextInt(ROWS);
            int startCol = random.nextInt(COLUMNS);
            while (mazeLayout[startRow][startCol] != 0) {
                startRow = random.nextInt(ROWS);
                startCol = random.nextInt(COLUMNS);
            }

            GridPane.setRowIndex(ghost, startRow);
            GridPane.setColumnIndex(ghost, startCol);
            mazeGrid.add(ghost, startCol, startRow);
        }

        // Set up ghost movement
        timeline = new Timeline(new KeyFrame(Duration.millis(1000 / level), e -> moveGhostsRandomly()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Create and set the scene
        Scene scene = new Scene(mazeGrid, TILE_SIZE * COLUMNS, TILE_SIZE * ROWS);
        primaryStage.setTitle("Pac-Man Ghost Initialization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Color getRandomGhostColor() {
        Color[] colors = {Color.RED, Color.PINK, Color.CYAN, Color.ORANGE};
        return colors[random.nextInt(colors.length)];
    }

    private void moveGhostsRandomly() {
        for (Circle ghost : ghosts) {
            int currentRow = GridPane.getRowIndex(ghost);
            int currentCol = GridPane.getColumnIndex(ghost);

            // Random movement: up, down, left, right
            int[] direction = getRandomDirection();
            int newRow = currentRow + direction[0];
            int newCol = currentCol + direction[1];

            // Check bounds and ensure the new position is valid
            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLUMNS && mazeLayout[newRow][newCol] == 0) {
                GridPane.setRowIndex(ghost, newRow);
                GridPane.setColumnIndex(ghost, newCol);
            }
        }

        // Increase ghost speed after each level
        level++;
        timeline.setRate(level);
    }

    private int[] getRandomDirection() {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right
        return directions[random.nextInt(directions.length)];
    }

    public static void main(String[] args) {
        launch(args);
    }
}
