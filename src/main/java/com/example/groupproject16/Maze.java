package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Maze extends Application {

    private static final int TILE_SIZE = 10;
    private static final int ROWS = 72;
    private static final int COLUMNS = 55;
    private String[][] mazeLayout;

    private int currentScore = 54;
    private int currentLevel = 0;

    @Override
    public void start(Stage primaryStage) {
        // Load the maze from the file
        mazeLayout = loadMazeFromFile("src/resources/PacManMap.txt");

        // Create the maze grid
        GridPane mazeGrid = new GridPane();

        // Variables to track Pac-Man's initial position
        int pacmanStartX = 0;
        int pacmanStartY = 0;

        // Draw the maze using the layout
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
                        tile.setFill(Color.YELLOW);
                        Circle dot = new Circle(TILE_SIZE / 4, Color.WHITE); // Small Yellow Dot
                        cell.getChildren().add(dot); // Add dot on top of the tile
                        break;
                    case 'P': // Pac-Man Start Position
                        tile.setFill(Color.BLACK);
                        pacmanStartX = col * TILE_SIZE; // Save Pac-Man's starting X position
                        pacmanStartY = row * TILE_SIZE; // Save Pac-Man's starting Y position
                        break;
                }
                cell.getChildren().add(tile); // Add the tile to the cell
                mazeGrid.add(cell, col, row); // Add the cell to the grid
            }
        }

        // Shift the grid down by setting its layout
        mazeGrid.setLayoutY(70); // Shift the grid down

        // Calculate the scene size to fit the entire maze
        int sceneWidth = TILE_SIZE * COLUMNS;
        int sceneHeight = TILE_SIZE * ROWS + 50; // Add extra height for the offset

        // Level and Score Text
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

        // Add Pac-Man as an ImageView
        Image pacManImage = new Image(getClass().getResource("/Images/moving-pacman.gif").toExternalForm());
        ImageView pacMan = new ImageView(pacManImage);
        pacMan.setFitWidth(20);
        pacMan.setFitHeight(20);
        pacMan.setX(pacmanStartX); // Set Pac-Man's starting X position
        pacMan.setY(pacmanStartY); // Set Pac-Man's starting Y position

        // Combine all elements into a Pane
        Pane root = new Pane();
        root.getChildren().addAll(mazeGrid, level, score, pacMan);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, sceneWidth, sceneHeight, Color.BLACK);
        primaryStage.setTitle("Pac-Man Maze");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add key listeners for Pac-Man movement
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP, W -> pacMan.setY(pacMan.getY() - TILE_SIZE);
                case DOWN, S -> pacMan.setY(pacMan.getY() + TILE_SIZE);
                case LEFT, A -> pacMan.setX(pacMan.getX() - TILE_SIZE);
                case RIGHT, D -> pacMan.setX(pacMan.getX() + TILE_SIZE);
            }
        });
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
                for (int col = 0; col < COLUMNS; col++) {
                    maze[row][col] = "E"; // Initialize to empty space as fallback
                }
            }
        }
        return maze;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

