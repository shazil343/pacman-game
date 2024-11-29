package com.example.groupproject16;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.logging.Level;

public class Maze extends Application {

    private static final int TILE_SIZE = 10;
    private static final int ROWS = 72; // Number of rows in the maze
    private static final int BLUEROWS = 52;
    private static final int COLUMNS = 54;// Number of columns in the maze



    private final int[][] mazeLayout = new int[ROWS][COLUMNS];

    @Override
    public void start(Stage primaryStage) {
        // Initialize the maze layout with the border walls
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                // Set border tiles to 1 (wall) and inner tiles to 0 (path)
                if (row == 0 || row == ROWS - 20 || col == 0 || col == COLUMNS - 1) {
                    mazeLayout[row][col] = 1; // Wall
                }
                else {
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
                if (mazeLayout[row][col] == 1 && row<BLUEROWS+1) {
                    tile.setFill(Color.BLUE);// Wall
                    if (row == BLUEROWS/2 && col == 0 || row == BLUEROWS/2 && col == COLUMNS-1) {
                        tile.setFill(Color.BLACK);
                    }
                    if (row == (BLUEROWS/2)-1 && col == 0 || row == (BLUEROWS/2)-1 && col == COLUMNS-1) {
                        tile.setFill(Color.BLACK);
                    }
                    if (row == (BLUEROWS/2)+1 && col == 0 || row == (BLUEROWS/2)+1 && col == COLUMNS-1) {
                        tile.setFill(Color.BLACK);
                    }

                } else {
                    tile.setFill(Color.BLACK); // Path
                }
                tile.setStroke(Color.BLACK); // Optional: Add a gridline effect
                mazeGrid.add(tile, col, row);
            }
        }
            //Level and Score Text
            Font customFont = Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 25);
            Text level = new Text("LEVEL");
            level.setFont(customFont);
            level.setFill(Color.WHITE);
            level.setX(10);
            level.setY(30);


            Text score = new Text("SCORE");
            score.setFont(customFont);
            score.setFill(Color.WHITE);
            score.setX(400);
            score.setY(30);

        // Use a Pane for absolute positioning
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        // Adjust the mazeGrid position so it does not overlap the texts
        mazeGrid.setLayoutY(50); // Move the maze down
        root.getChildren().addAll(mazeGrid, level,score);

        // Create and set the scene
        Scene scene = new Scene(root,600,700);
        primaryStage.setTitle("Pac-Man Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}