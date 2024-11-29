package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Maze extends Application {

    private static final int TILE_SIZE = 10;
    private static final int ROWS = 72; // Number of rows in the maze
    private static final int BLUEROWS = 52;
    private static final int COLUMNS = 54; // Number of columns in the maze

    private final int[][] mazeLayout = new int[ROWS][COLUMNS];

    @Override
    public void start(Stage primaryStage) {
        // Initialize the maze layout with the border walls and central box
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                // Set border tiles to 1 (wall) and inner tiles to 0 (path)
                if (row == 0 || row == ROWS - 20 || col == 0 || col == COLUMNS - 1) {
                    mazeLayout[row][col] = 1; // Wall
                }
                // Define the 12x12 central box with blue borders
                else if ((row >= (BLUEROWS / 2) - 6 && row <= (BLUEROWS / 2) + 5) &&
                        (col >= (COLUMNS / 2) - 6 && col <= (COLUMNS / 2) + 5)) {
                    if (row == (BLUEROWS / 2) - 6 && // Top border with a 3-unit gap
                            (col < (COLUMNS / 2) - 2 || col > (COLUMNS / 2))) {
                        mazeLayout[row][col] = 1; // Top border wall, leave a 3-unit gap
                    } else if (row == (BLUEROWS / 2) + 5 || // Bottom border
                            col == (COLUMNS / 2) - 6 || col == (COLUMNS / 2) + 5) { // Left/Right borders
                        mazeLayout[row][col] = 1; // Wall
                    } else {
                        mazeLayout[row][col] = 0; // Inside of the box (path)
                    }
                } else {
                    mazeLayout[row][col] = 0; // Path
                }
            }
        }

        // Create the maze grid
        GridPane mazeGrid = new GridPane();

        // Draw the maze using the layout
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                if (mazeLayout[row][col] == 1 && row < BLUEROWS + 1) {
                    tile.setFill(Color.BLUE); // Wall
                    if (row == BLUEROWS / 2 && col == 0 || row == BLUEROWS / 2 && col == COLUMNS - 1) {
                        tile.setFill(Color.BLACK);
                    }
                    if (row == (BLUEROWS / 2) - 1 && col == 0 || row == (BLUEROWS / 2) - 1 && col == COLUMNS - 1) {
                        tile.setFill(Color.BLACK);
                    }
                    if (row == (BLUEROWS / 2) + 1 && col == 0 || row == (BLUEROWS / 2) + 1 && col == COLUMNS - 1) {
                        tile.setFill(Color.BLACK);
                    }

                } else {
                    tile.setFill(Color.BLACK); // Path
                }
                tile.setStroke(Color.BLACK); // Optional: Add a gridline effect
                mazeGrid.add(tile, col, row);
            }
        }

        // Level and Score Text
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 20);
        Text level = new Text("LEVEL");
        level.setFont(customFont);
        level.setFill(Color.WHITE);
        level.setX(20);
        level.setY(50);

        Text score = new Text("SCORE");
        score.setFont(customFont);
        score.setFill(Color.WHITE);
        score.setX(50);
        score.setY(50);

        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(score, level, mazeGrid);

        // Create and set the scene
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("Pac-Man Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
