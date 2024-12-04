
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.example.groupproject16.InitializeGhosts.GHOST_COUNT;

public class Maze extends Application {

    private static final int TILE_SIZE = 10;
    private static final int ROWS = 72;
    private static final int COLUMNS = 55;
    private static final int GHOST_COUNT = 4;
    private final Map<String, Character> mazeMap = new HashMap<>();

    private ImageView pacMan;
    private String currentDirection = null;
    private Timeline movementTimeline;

    private int pacManRow;
    private int pacManCol;

    private int currentScore = 54;
    private int currentLevel = 0;

    private Text scoreText; // Made scoreText a class-level variable

@Override
public void start(Stage primaryStage) {
    loadMazeFromFile("src/resources/PacManMapcopy.txt");

    if (!findStartPosition()) {
        System.err.println("Pac-Man's starting position ('P') not found in the maze!");
        return; // Exit if no starting position is found
    }

        // Build maze visuals
        GridPane mazeGrid = createMazeGrid();

        // Create Pac-Man
        pacMan = createPacMan();
        pacMan.setX(pacManCol * TILE_SIZE);
        pacMan.setY(pacManRow * TILE_SIZE + 50);

        Pane root = new Pane();
        root.getChildren().addAll(mazeGrid, pacMan);

        // Add UI components
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/Fonts/MegaMaxJonathanToo-YqOq2.ttf"), 25);
        Text level = new Text("LEVEL: " + currentLevel);
        level.setFont(customFont);
        level.setFill(Color.WHITE);
        level.setX(10);
        level.setY(30);

        scoreText = new Text("SCORE: " + currentScore);
        scoreText.setFont(customFont);
        scoreText.setFill(Color.WHITE);
        scoreText.setX(350);
        scoreText.setY(30);

        root.getChildren().addAll(level, scoreText);

        // Initialize ghosts
        initializeGhosts(root);

        Scene scene = new Scene(root, TILE_SIZE * COLUMNS, TILE_SIZE * ROWS , Color.BLACK);
        scene.setOnKeyPressed(this::handleKeyPress);

        // Continuous movement timeline
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> movePacMan()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();

        primaryStage.setTitle("Pac-Man Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeGhosts(Pane root) {
        ImageView[] ghosts = InitializeGhosts.createGhosts(GHOST_COUNT, TILE_SIZE);
        Random random = new Random();
        for (ImageView ghost : ghosts) {
            int ghostRow, ghostCol;
            do {
                ghostRow = random.nextInt(ROWS);
                ghostCol = random.nextInt(COLUMNS);
            } while (mazeMap.getOrDefault(ghostRow + "," + ghostCol, 'W') == 'W');

            ghost.setX(ghostCol * TILE_SIZE);
            ghost.setY(ghostRow * TILE_SIZE + 50); // Account for offset
            root.getChildren().add(ghost);
        }
    }

    private GridPane createMazeGrid() {
        GridPane mazeGrid = new GridPane();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                char tileType = mazeMap.getOrDefault(row + "," + col, 'E');

                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                StackPane cell = new StackPane();

                switch (tileType) {
                    case 'W' -> tile.setFill(Color.BLUE);
                    case 'E' -> tile.setFill(Color.BLACK);
                    case 'S' -> {
                        tile.setFill(Color.YELLOW);
                        Circle dot = new Circle(TILE_SIZE / 4, Color.YELLOW);
                        cell.getChildren().add(dot);
                    }
                }
                cell.getChildren().add(tile);
                mazeGrid.add(cell, col, row);
            }
        }
        mazeGrid.setLayoutY(50); // Positioning the maze below the UI texts
        return mazeGrid;
    }


    private boolean findStartPosition() {
        boolean found = false;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                char tile = mazeMap.getOrDefault(row + "," + col, 'E');
                if (tile == 'P') {
                    pacManRow = row;
                    pacManCol = col;
                    found = true;
                    System.out.println("Pac-Man starts at: Row " + row + ", Column " + col);
                    break;
                }
            }
            if (found) break;
        }
        if (!found) {
            System.err.println("Failed to find 'P' in the maze.");
        }
        return found;
    }


    private ImageView createPacMan() {
        Image pacManImage = new Image(getClass().getResource("/Images/moving-pacman.gif").toExternalForm());
        ImageView pacMan = new ImageView(pacManImage);
        pacMan.setFitWidth(TILE_SIZE * 2.5);
        pacMan.setFitHeight(TILE_SIZE * 2.5);
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

        // Check if the move is within the bounds and not hitting a wall
        if (isValidMove(nextRow, nextCol)) {
            pacManRow = nextRow;
            pacManCol = nextCol;
            pacMan.setX(pacManCol * TILE_SIZE + TILE_SIZE / 2 - pacMan.getFitWidth() / 2);
            pacMan.setY(pacManRow * TILE_SIZE + TILE_SIZE / 2 - pacMan.getFitHeight() / 2 + 50);
        }
    }

    private boolean isValidMove(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLUMNS) {
            return false; // Ensure Pac-Man does not move out of maze bounds.
        }
        char tileType = mazeMap.getOrDefault(row + "," + col, 'W'); // Default to wall if no data.
        return tileType == 'S' || tileType == 'P' || tileType == 'N'; // Specify which tile types are walkable.
    }

    private void updateScore() {
        scoreText.setText("SCORE: " + currentScore);
    }

    private void loadMazeFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] rowElements = line.trim().split("\\s+");
                for (int col = 0; col < rowElements.length; col++) {
                    char tile = rowElements[col].isEmpty() ? 'E' : rowElements[col].charAt(0);
                    mazeMap.put(row + "," + col, tile);
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Error reading maze file: " + e.getMessage());
            // Initialize the map with empty space if error occurs
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    mazeMap.put(i + "," + j, 'E');
                }
            }
        }
    }}


