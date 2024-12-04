package com.example.groupproject16;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class HumanPlayer {

    private static final int TILE_SIZE = 10; // Movement aligns with the maze grid
    private KeyCode currentDirection = null; // Track the current movement direction as KeyCode
    private Timeline movementTimeline; // Timeline for continuous movement
    private final ImageView pacMan; // Pac-Man image view
    private final int sceneWidth;
    private final int sceneHeight;

    public HumanPlayer(int sceneWidth, int sceneHeight) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;

        // Initialize Pac-Man
        Image pacManImage = new Image(getClass().getResource("/Images/moving-pacman.gif").toExternalForm());
        pacMan = new ImageView(pacManImage);
        pacMan.setFitWidth(TILE_SIZE);
        pacMan.setFitHeight(TILE_SIZE);

        // Movement timeline
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(200), e -> movePacMan()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public ImageView getPacMan() {
        return pacMan;
    }

    public void setStartPosition(int startX, int startY) {
        pacMan.setX(startX * TILE_SIZE);
        pacMan.setY(startY * TILE_SIZE);
    }

    public void handleKeyPress(KeyEvent event) {
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



    private void movePacMan() {
        if (currentDirection == null) return;

        double nextX = pacMan.getX();
        double nextY = pacMan.getY();

        switch (currentDirection) {
            case UP -> nextY -= TILE_SIZE;
            case DOWN -> nextY += TILE_SIZE;
            case LEFT -> nextX -= TILE_SIZE;
            case RIGHT -> nextX += TILE_SIZE;
        }

        // Constrain Pac-Man within the scene bounds
        if (nextX >= 0 && nextX <= sceneWidth - TILE_SIZE &&
                nextY >= 0 && nextY <= sceneHeight - TILE_SIZE) {
            pacMan.setX(nextX);
            pacMan.setY(nextY);
        }
    }
}
