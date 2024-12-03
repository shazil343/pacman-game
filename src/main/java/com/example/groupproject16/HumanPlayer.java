package com.example.groupproject16;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HumanPlayer extends Application {

    private static final int SCENE_WIDTH = 600;
    private static final int SCENE_HEIGHT = 400;
    private static final int STEP = 5; // Movement step size

    private KeyCode currentDirection = null; // Track the current movement direction as KeyCode
    private Timeline movementTimeline; // Timeline for continuous movement

    public void start(Stage primaryStage) {
        Image pacManImage = new Image(getClass().getResource("/Images/moving-pacman.gif").toExternalForm());
        ImageView pacMan = new ImageView(pacManImage);
        pacMan.setFitWidth(20);
        pacMan.setFitHeight(20);
        pacMan.setX(SCENE_WIDTH / 2 - 20); // Center horizontally
        pacMan.setY(SCENE_HEIGHT / 2 - 20); // Center vertically

        Pane root = new Pane();
        root.getChildren().add(pacMan);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setOnKeyPressed(event -> handleKeyPress(event, pacMan));

        movementTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> movePacMan(pacMan)));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);

        primaryStage.setTitle("Pac-Man Continuous Movement with Rotation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleKeyPress(KeyEvent event, ImageView pacMan) {
        KeyCode key = event.getCode();
        switch (key) {
            case UP:
            case W:
                currentDirection = key;
                pacMan.setRotate(270);
                break;
            case DOWN:
            case S:
                currentDirection = key;
                pacMan.setRotate(90);
                break;
            case LEFT:
            case A:
                currentDirection = key;
                pacMan.setRotate(180);
                break;
            case RIGHT:
            case D:
                currentDirection = key;
                pacMan.setRotate(0);
                break;
        }

        if (movementTimeline.getStatus() != Timeline.Status.RUNNING) {
            movementTimeline.play();
        }
    }

    private void movePacMan(ImageView pacMan) {
        if (currentDirection == null) return;

        switch (currentDirection) {
            case UP:
                pacMan.setY(pacMan.getY() - STEP);
                break;
            case DOWN:
                pacMan.setY(pacMan.getY() + STEP);
                break;
            case LEFT:
                pacMan.setX(pacMan.getX() - STEP);
                break;
            case RIGHT:
                pacMan.setX(pacMan.getX() + STEP);
                break;
        }

        // Constrain Pac-Man within the scene bounds
        pacMan.setX(Math.max(0, Math.min(pacMan.getX(), SCENE_WIDTH - pacMan.getFitWidth())));
        pacMan.setY(Math.max(0, Math.min(pacMan.getY(), SCENE_HEIGHT - pacMan.getFitHeight())));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
