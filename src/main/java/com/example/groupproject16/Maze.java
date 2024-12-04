
package com.example.groupproject16;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Maze class represents the game maze where Pac-Man operates.
 */
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
        loadMazeFromFile("/PacManMap.txt"); // Use class loader to load resource

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

        // Continuous movement timeline for Pac-Man and ghosts
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            movePacMan();
            moveGhosts(); // Call ghost movement
        }));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();

        Scene scene = new Scene(root, TILE_SIZE * COLUMNS, TILE_SIZE * ROWS + 50, Color.BLACK);
        scene.setOnKeyPressed(this::handleKeyPress);

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

        // Collect all positions marked with '1' in the mazeMap
        List<String> spawnPoints = new ArrayList<>();
        for (Map.Entry<String, Character> entry : mazeMap.entrySet()) {
            if (entry.getValue() == '1') {
                spawnPoints.add(entry.getKey());
            }
        }

        // Check if there are enough spawn points
        if (spawnPoints.size() < GHOST_COUNT) {
            System.out.println("Not enough '1' spawn points. Ghosts will be placed at default positions.");
        }

        for (int i = 0; i < GHOST_COUNT; i++) {
            ImageView ghost = ghosts[i];
            if (i < spawnPoints.size()) {
                String[] parts = spawnPoints.get(i).split(",");
                int ghostRow = Integer.parseInt(parts[0]);
                int ghostCol = Integer.parseInt(parts[1]);

                ghost.setX(ghostCol * TILE_SIZE);
                ghost.setY(ghostRow * TILE_SIZE + 50); // Account for Y-offset
            } else {
                // If not enough spawn points, place ghost at center or another default position
                int centerRow = ROWS / 2;
                int centerCol = COLUMNS / 2;
                ghost.setX(centerCol * TILE_SIZE);
                ghost.setY(centerRow * TILE_SIZE + 50);
                System.out.println("Ghost " + (i + 1) + " placed at default position: Row " + centerRow + ", Column " + centerCol);
            }
            root.getChildren().add(ghost);
        }

        // Optional: Warn if there are more spawn points than ghosts
        if (spawnPoints.size() > GHOST_COUNT) {
            System.out.println("There are more '1' spawn points than ghosts. Extra spawn points are unused.");
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
                        tile.setFill(Color.YELLOW);
                        Circle dot = new Circle(TILE_SIZE / 4, Color.YELLOW);
                        cell.getChildren().add(dot);
                    }
                    case '1' -> {
                        tile.setFill(Color.DARKGRAY); // Distinct color for spawn points
                        Circle spawnMarker = new Circle(TILE_SIZE / 2, Color.RED);
                        cell.getChildren().add(spawnMarker); // Visual marker
                    }
                    case 'P' -> {
                        tile.setFill(Color.BLACK);
                        // Optionally, mark Pac-Man's start position visually
                        Circle pacManMarker = new Circle(TILE_SIZE / 2, Color.GREEN);
                        cell.getChildren().add(pacManMarker);
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
            pacManRow = nextRow;
            pacManCol = nextCol;
            pacMan.setX(pacManCol * TILE_SIZE);
            pacMan.setY(pacManRow * TILE_SIZE + 50); // Added Y-offset

            // Handle dot collection if moving onto 'S'
            char tileType = mazeMap.getOrDefault(pacManRow + "," + pacManCol, 'E');
            if (tileType == 'S') {
                currentScore += 10; // Increment score for collecting a dot
                mazeMap.put(pacManRow + "," + pacManCol, 'E'); // Remove the dot from the maze map
                updateScore(); // Update the score Text node
                // Optionally, update the visual representation by removing the dot's node
            }
        }
    }

    /**
     * Moves all ghosts towards Pac-Man.
     */
    private void moveGhosts() {
        ImageView[] ghosts = getGhostNodes();
        for (ImageView ghost : ghosts) {
            if (ghost == null) continue;

            double ghostX = ghost.getX();
            double ghostY = ghost.getY();
            double pacManX = pacMan.getX();
            double pacManY = pacMan.getY();

            int ghostRow = getRowFromY(ghostY);
            int ghostCol = getColFromX(ghostX);

            // Determine direction towards Pac-Man
            String direction = null;
            if (ghostX < pacManX) direction = "RIGHT";
            else if (ghostX > pacManX) direction = "LEFT";
            if (ghostY < pacManY) direction = "DOWN";
            else if (ghostY > pacManY) direction = "UP";

            // Attempt to move in the determined direction if valid
            int nextRow = ghostRow;
            int nextCol = ghostCol;

            switch (direction) {
                case "UP" -> nextRow--;
                case "DOWN" -> nextRow++;
                case "LEFT" -> nextCol--;
                case "RIGHT" -> nextCol++;
            }

            if (isValidMove(nextRow, nextCol)) {
                ghostRow = nextRow;
                ghostCol = nextCol;
                ghost.setX(ghostCol * TILE_SIZE);
                ghost.setY(ghostRow * TILE_SIZE + 50);
            }

            // Collision detection between ghost and Pac-Man
            if (isCollision(pacMan, ghost)) {
                System.out.println("Pac-Man has been caught by a ghost! Game Over.");
                movementTimeline.stop();
                // Implement additional game over logic here (e.g., display Game Over screen)
            }
        }
    }

    /**
     * Retrieves all ghost ImageView nodes from the maze pane.
     *
     * @return An array of ghost ImageViews.
     */
    private ImageView[] getGhostNodes() {
        Pane root = (Pane) pacMan.getParent();
        ImageView[] ghosts = new ImageView[GHOST_COUNT];
        int index = 0;
        for (Node node : root.getChildren()) {
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
     * Checks if moving to the specified position is valid.
     *
     * @param row The target row.
     * @param col The target column.
     * @return true if valid, false otherwise.
     */
    boolean isValidMove(int row, int col) {
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
            printMazeMap(); // For debugging
        } catch (IOException e) {
            System.err.println("Error reading maze file: " + e.getMessage());
            // Initialize the map with empty space if error occurs
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    mazeMap.put(i + "," + j, 'E');
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Maze file not found: " + fileName);
            // Initialize the map with empty space if file not found
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    mazeMap.put(i + "," + j, 'E');
                }
            }
        }
    }

    /**
     * Prints the maze map to the console for debugging purposes.
     */
    private void printMazeMap() {
        for (int row = 0; row < ROWS; row++) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < COLUMNS; col++) {
                sb.append(mazeMap.getOrDefault(row + "," + col, 'E')).append(' ');
            }
            System.out.println(sb.toString().trim());
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

    public static void main(String[] args) {
        launch(args);
    }
}

