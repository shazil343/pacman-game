package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Maze extends Application {

    private static final int TILE_SIZE = 10;
    private static final int ROWS = 72;
    private static final int COLUMNS = 54;
    private String[][] mazeLayout;

    @Override
    public void start(Stage primaryStage) {
        // Load the maze from the file
        mazeLayout = loadMazeFromFile("src/resources/PacManMap.txt");

        // Create the maze grid
        GridPane mazeGrid = new GridPane();

        // Draw the maze using the layout
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                char tileType = mazeLayout[row][col] != null ? mazeLayout[row][col].charAt(0) : 'E';

                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                switch (tileType) {
                    case 'W': // Wall
                        tile.setFill(Color.BLUE);
                        break;
                    case 'E': // Empty Space
                        tile.setFill(Color.BLACK);
                        break;
                    case 'S': // Small Dot
                        tile.setFill(Color.BLACK); // Background
                        Circle dot = new Circle(TILE_SIZE / 4, Color.YELLOW); // Small Yellow Dot
                        dot.setTranslateX((TILE_SIZE - dot.getRadius() * 2) / 2); // Center horizontally
                        dot.setTranslateY((TILE_SIZE - dot.getRadius() * 2) / 2); // Center vertically
                        mazeGrid.add(dot, col, row); // Add dot to the same cell
                        break;
                }
                mazeGrid.add(tile, col, row); // Add the tile
            }
        }

        // Create the scene and set it on the stage
        Scene scene = new Scene(mazeGrid, TILE_SIZE * COLUMNS, TILE_SIZE * ROWS);
        primaryStage.setTitle("Pac-Man Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
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
            // Initialize all tiles to "E" (empty space) as a fallback
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    maze[row][col] = "E";
                }
            }
        }
        return maze;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
