package com.example.groupproject16;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HumanPlayer extends Application {

    private static final int SCENE_WIDTH = 600;
    private static final int SCENE_HEIGHT = 400;
    private static final int STEP = 5; // Movement step size

    private String currentDirection = null; // Track the current movement direction
    private Timeline movementTimeline; // Timeline for continuous movement

    @Override
    public void start(Stage primaryStage) {
        // Load Pac-Man GIF
        Image pacManImage = new Image(getClass().getResource("/Images/moving-pacman.gif").toExternalForm());
        ImageView pacMan = new ImageView(pacManImage);

        // Set initial position and size for Pac-Man
        pacMan.setFitWidth(20);
        pacMan.setFitHeight(20);
        pacMan.setX(SCENE_WIDTH / 2 - 20); // Center horizontally
        pacMan.setY(SCENE_HEIGHT / 2 - 20); // Center vertically

        // Create a Pane to hold the Pac-Man
        Pane root = new Pane();
        root.getChildren().add(pacMan);

        // Create a scene
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        // Timeline for continuous movement
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> movePacMan(pacMan)));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);

        // Add key event handlers for movement
        scene.setOnKeyPressed(event -> startMoving(event, pacMan));

        // Set up the stage
        primaryStage.setTitle("Pac-Man Continuous Movement with Rotation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startMoving(KeyEvent event, ImageView pacMan) {
        switch (event.getCode()) {
            case UP, W -> {
                currentDirection = "UP";
                pacMan.setRotate(270); // Rotate image upwards
            }
            case DOWN, S -> {
                currentDirection = "DOWN";
                pacMan.setRotate(90); // Rotate image downwards
            }
            case LEFT, A -> {
                currentDirection = "LEFT";
                pacMan.setRotate(180); // Rotate image to the left
            }
            case RIGHT, D -> {
                currentDirection = "RIGHT";
                pacMan.setRotate(0); // Rotate image to the right
            }
        }

        // Start the timeline if not already running
        if (movementTimeline.getStatus() != Timeline.Status.RUNNING) {
            movementTimeline.play();
        }
    }

    private void movePacMan(ImageView pacMan) {
        if (currentDirection == null) return;

        switch (currentDirection) {
            case "UP" -> pacMan.setY(pacMan.getY() - STEP);
            case "DOWN" -> pacMan.setY(pacMan.getY() + STEP);
            case "LEFT" -> pacMan.setX(pacMan.getX() - STEP);
            case "RIGHT" -> pacMan.setX(pacMan.getX() + STEP);
        }

        // Prevent Pac-Man from moving out of bounds
        double newX = Math.max(0, Math.min(pacMan.getX(), SCENE_WIDTH - pacMan.getFitWidth()));
        double newY = Math.max(0, Math.min(pacMan.getY(), SCENE_HEIGHT - pacMan.getFitHeight()));
        pacMan.setX(newX);
        pacMan.setY(newY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
