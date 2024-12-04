package com.example.groupproject16;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node; // Added import to resolve the compilation error
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
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The Maze class represents the game maze where Pac-Man operates.
 */
public class Maze extends Application {

    private static final int TILE_SIZE = 10;
    private static final int ROWS = 72;
    private static final int COLUMNS = 55;
    private static final int GHOST_COUNT = 4; // Number of ghosts to spawn
    private final Map<String, Character> mazeMap = new HashMap<>();

    private ImageView pacMan;
    private String currentDirection = null;
    private Timeline movementTimeline;

    private int pacManRow;
    private int pacManCol;

    private int currentScore = 54; // Starting score
    private int currentLevel = 0;  // Starting level

    private Text scoreText;

    @Override
    public void start(Stage primaryStage) {
        // Load the maze from the file
        loadMazeFromFile("/PacManMap.txt"); // Ensure the path is correct and starts with '/'

        // Verify that maze is loaded properly
        if (!findStartPosition()) {
            throw new IllegalStateException("Pac-Man's starting position ('P') not found in the maze!");
        }

        // Build maze visuals
        GridPane mazeGrid = createMazeGrid();

        // Create Pac-Man
        pacMan = createPacMan();
        pacMan.setX(pacManCol * TILE_SIZE);
        pacMan.setY(pacManRow * TILE_SIZE + 50); // Added Y-offset for UI placement

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

        Scene scene = new Scene(root, TILE_SIZE * COLUMNS, TILE_SIZE * ROWS + 50, Color.BLACK);
        scene.setOnKeyPressed(this::handleKeyPress);

        // Continuous movement timeline for Pac-Man and ghosts
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            movePacMan();
            moveGhosts(); // Call ghost movement
        }));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();

        primaryStage.setTitle("Pac-Man Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Initializes ghosts and adds them to the maze pane at positions marked with '1'.
     *
     * @param root The main pane of the maze.
     */
    private void initializeGhosts(Pane root) {
        ImageView[] ghosts = InitializeGhosts.createGhosts(GHOST_COUNT, TILE_SIZE);
        Random random = new Random();

        // Collect all positions marked with '1' in the mazeMap
        // These will be the spawn points for ghosts
        int[] spawnRows = new int[GHOST_COUNT];
        int[] spawnCols = new int[GHOST_COUNT];
        int spawnIndex = 0;

        for (Map.Entry<String, Character> entry : mazeMap.entrySet()) {
            if (entry.getValue() == '1') {
                String[] parts = entry.getKey().split(",");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if (spawnIndex < GHOST_COUNT) {
                    spawnRows[spawnIndex] = row;
                    spawnCols[spawnIndex] = col;
                    spawnIndex++;
                }

                if (spawnIndex >= GHOST_COUNT) {
                    break; // Enough spawn points collected
                }
            }
        }

        // If not enough '1' spawn points, default to center
        if (spawnIndex < GHOST_COUNT) {
            System.out.println("Not enough '1' spawn points. Defaulting remaining ghosts to center.");
            int centerRow = ROWS / 2;
            int centerCol = COLUMNS / 2;
            while (spawnIndex < GHOST_COUNT) {
                spawnRows[spawnIndex] = centerRow;
                spawnCols[spawnIndex] = centerCol;
                spawnIndex++;
            }
        }

        // Position each ghost at the collected spawn points
        for (int i = 0; i < GHOST_COUNT; i++) {
            ImageView ghost = ghosts[i];
            int ghostRow = spawnRows[i];
            int ghostCol = spawnCols[i];

            ghost.setX(ghostCol * TILE_SIZE);
            ghost.setY(ghostRow * TILE_SIZE + 50); // Account for offset

            root.getChildren().add(ghost);
        }
    }

    /**
     * Creates the GridPane representing the maze based on the maze layout.
     *
     * @return GridPane of the maze.
     */
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
                        tile.setFill(Color.BLACK);
                        Circle dot = new Circle(TILE_SIZE / 4, Color.YELLOW);
                        cell.getChildren().add(dot);
                    }
                    case '1' -> {
                        tile.setFill(Color.PURPLE); // Distinct color for spawn points
                        // Optional: Add a visual marker for spawn points
                        Circle spawnMarker = new Circle(TILE_SIZE / 2, Color.WHITE);
                        cell.getChildren().add(spawnMarker);
                    }
                }
                cell.getChildren().add(tile);
                mazeGrid.add(cell, col, row);
            }
        }
        mazeGrid.setLayoutY(50); // Positioning the maze below the UI texts
        return mazeGrid;
    }

    /**
     * Finds the starting position of Pac-Man marked by 'P' in the maze.
     *
     * @return true if found, false otherwise.
     */
    private boolean findStartPosition() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (mazeMap.getOrDefault(row + "," + col, 'E') == 'P') {
                    pacManRow = row;
                    pacManCol = col;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates the Pac-Man ImageView.
     *
     * @return ImageView of Pac-Man.
     */
    private ImageView createPacMan() {
        Image pacManImage = new Image(getClass().getResource("/Images/moving-pacman.gif").toExternalForm());
        ImageView pacMan = new ImageView(pacManImage);
        pacMan.setFitWidth(TILE_SIZE * 2.5);
        pacMan.setFitHeight(TILE_SIZE * 2.5);
        return pacMan;
    }

    /**
     * Handles key press events to set Pac-Man's direction.
     *
     * @param event The KeyEvent.
     */
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

    /**
     * Moves Pac-Man in the current direction if the move is valid.
     */
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

        if (isValidMove(nextRow, nextCol)) {
            // Check if Pac-Man is moving onto a dot
            if (mazeMap.getOrDefault(nextRow + "," + nextCol, 'E') == 'S') {
                currentScore += 10; // Increment score for collecting a dot
                mazeMap.put(nextRow + "," + nextCol, 'E'); // Remove the dot from the maze map
                // Optionally, remove the dot's visual representation
                // This would require keeping track of the dots' nodes, possibly using a separate map
            }

            pacManRow = nextRow;
            pacManCol = nextCol;
            pacMan.setX(pacManCol * TILE_SIZE);
            pacMan.setY(pacManRow * TILE_SIZE + 50); // Added Y-offset
            updateScore(); // Update the score Text node
        }
    }

    /**
     * Moves all ghosts towards Pac-Man.
     */
    private void moveGhosts() {
        // Placeholder for ghost movement logic
        // You can implement movement algorithms here
        // For example, simple logic to move ghosts towards Pac-Man's current position
        // Ensure to call isValidMove() before moving to avoid walls

        // Example simple movement logic:
        for (Node ghostNode : getGhostNodes()) {
            if (ghostNode instanceof ImageView ghost) {
                double ghostX = ghost.getX();
                double ghostY = ghost.getY();
                double pacManX = pacMan.getX();
                double pacManY = pacMan.getY();

                // Calculate direction towards Pac-Man
                if (ghostX < pacManX && isValidMove(getRowFromY(ghostY), getColFromX(ghostX + TILE_SIZE))) {
                    ghost.setX(ghostX + TILE_SIZE);
                } else if (ghostX > pacManX && isValidMove(getRowFromY(ghostY), getColFromX(ghostX - TILE_SIZE))) {
                    ghost.setX(ghostX - TILE_SIZE);
                }

                if (ghostY < pacManY && isValidMove(getRowFromY(ghostY + TILE_SIZE), getColFromX(ghostX))) {
                    ghost.setY(ghostY + TILE_SIZE);
                } else if (ghostY > pacManY && isValidMove(getRowFromY(ghostY - TILE_SIZE), getColFromX(ghostX))) {
                    ghost.setY(ghostY - TILE_SIZE);
                }

                // Collision detection between ghost and Pac-Man
                if (isCollision(pacMan, ghost)) {
                    System.out.println("Pac-Man has been caught by a ghost! Game Over.");
                    movementTimeline.stop();
                    // Implement additional game over logic here (e.g., display Game Over screen)
                }
            }
        }
    }

    /**
     * Retrieves all ghost ImageView nodes from the maze pane.
     *
     * @return An array of ghost ImageViews.
     */
    private ImageView[] getGhostNodes() {
        // Assuming ghosts are added after Pac-Man in the root pane
        // Adjust this method based on how ghosts are added to the pane
        // For simplicity, we'll iterate through all children and collect ImageViews excluding Pac-Man

        Pane root = (Pane) pacMan.getParent();
        ImageView[] ghosts = new ImageView[GHOST_COUNT];
        int index = 0;
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof ImageView imageView && imageView != pacMan) {
                ghosts[index++] = imageView;
                if (index >= GHOST_COUNT) break;
            }
        }
        return ghosts;
    }

    /**
     * Checks if two ImageViews collide.
     *
     * @param a First ImageView.
     * @param b Second ImageView.
     * @return true if they collide, false otherwise.
     */
    private boolean isCollision(ImageView a, ImageView b) {
        return a.getBoundsInParent().intersects(b.getBoundsInParent());
    }

    /**
     * Updates the score display.
     */
    private void updateScore() {
        scoreText.setText("SCORE: " + currentScore);
    }

    /**
     * Loads the maze layout from a text file.
     *
     * @param fileName The path to the maze file within the resources directory.
     */
    private void loadMazeFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < ROWS) {
                String[] rowElements = line.trim().split("\\s+");
                for (int col = 0; col < COLUMNS && col < rowElements.length; col++) {
                    char tile = rowElements[col].isEmpty() ? 'E' : rowElements[col].charAt(0);
                    mazeMap.put(row + "," + col, tile);
                }
                row++;
            }
            System.out.println("Maze file loaded successfully.");
        } catch (IOException | NullPointerException e) {
            System.out.println("Error reading maze file: " + e.getMessage());
        }
    }

    /**
     * Converts Y-coordinate to maze row index.
     *
     * @param y Y-coordinate.
     * @return Row index.
     */
    private int getRowFromY(double y) {
        return (int) ((y - 50) / TILE_SIZE);
    }

    /**
     * Converts X-coordinate to maze column index.
     *
     * @param x X-coordinate.
     * @return Column index.
     */
    private int getColFromX(double x) {
        return (int) (x / TILE_SIZE);
    }

    /**
     * Checks if moving to the specified position is valid.
     *
     * @param row The target row.
     * @param col The target column.
     * @return true if valid, false otherwise.
     */
    private boolean isValidMove(int row, int col) {
        // Boundary checks
        if (row < 0 || row >= ROWS || col < 0 || col >= COLUMNS) {
            System.out.println("Attempted to move out of bounds to (" + row + ", " + col + ").");
            return false;
        }

        char tileType = mazeMap.getOrDefault(row + "," + col, 'E');
        if (tileType == 'W') {
            System.out.println("Movement blocked by wall at (" + row + ", " + col + ").");
            return false;
        }

        // Allow movement into 'S' (dot), 'E' (empty), 'P' (Pac-Man's position), or '1' (ghost spawn points)
        return tileType == 'S' || tileType == 'E' || tileType == 'P' || tileType == '1';
    }

    public static void main(String[] args) {
        launch(args);
    }
}

