package com.example.groupproject16;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Maze extends Application {

    private static final int TILE_SIZE = 30; // Size of each square in the grid
    private static final int ROWS = 20; // Number of rows in the maze
    private static final int COLUMNS = 20; // Number of columns in the maze

    @Override
    public void start(Stage primaryStage) {
        // Create the grid for the maze
        GridPane mazeGrid = new GridPane();

        // Maze layout: 0 = empty path, 1 = wall, 2 = Pac-Man start, 3 = ghost zone
        int[][] mazeLayout = {
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

        // Build the maze based on the layout
        for (int row = 0; row < mazeLayout.length; row++) {
            for (int col = 0; col < mazeLayout[row].length; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                switch (mazeLayout[row][col]) {
                    case 0: // Path
                        tile.setFill(Color.BLACK);
                        break;
                    case 1: // Wall
                        tile.setFill(Color.BLUE);
                        break;
                    case 2: // Pac-Man start
                        tile.setFill(Color.YELLOW);
                        break;
                    case 3: // Ghost zone
                        tile.setFill(Color.RED);
                        break;
                }
                tile.setStroke(Color.BLACK); // Optional: add a gridline effect
                mazeGrid.add(tile, col, row);
            }
        }

        // Create and set the scene
        Scene scene = new Scene(mazeGrid, TILE_SIZE * COLUMNS, TILE_SIZE * ROWS);
        primaryStage.setTitle("Pac-Man Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

