package com.example.groupproject16;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class InitializeGhosts extends Application {

    private static final int SCENE_WIDTH = 600;
    private static final int SCENE_HEIGHT = 400;
    private static final int STEP = 5; // Movement step size
    private static final int GHOST_COUNT = 4; // Number of ghosts

    public void start(Stage primaryStage) {
        // Create a Pane to hold the ghosts
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        // Random number generator for ghost directions
        Random random = new Random();

        // Array to hold file paths for ghost GIFs
        String[] ghostImages = {"green-ghost.gif", "orange-ghost.gif","pink-ghost.gif","red-ghost.gif"};

        // Array to hold ghost ImageViews
        ImageView[] ghosts = new ImageView[GHOST_COUNT];
        Timeline[] timelines = new Timeline[GHOST_COUNT];

        // Initialize each ghost
        for (int i = 0; i < GHOST_COUNT; i++) {
            // Load the ghost GIF
            Image ghostImage = new Image(ghostImages[i]); // Use file paths from the array
            ImageView ghost = new ImageView(ghostImage);

            // Set initial position and size for each ghost
            ghost.setFitWidth(40);
            ghost.setFitHeight(40);
            ghost.setX(random.nextInt(SCENE_WIDTH - 40)); // Random initial X position
            ghost.setY(random.nextInt(SCENE_HEIGHT - 40)); // Random initial Y position

            // Add ghost to the array and the Pane
            ghosts[i] = ghost;
            root.getChildren().add(ghost);

            // Create a Timeline for random movement
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> moveGhost(ghost, random)));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            // Store the timeline
            timelines[i] = timeline;
        }

        // Create the scene
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        // Set up the stage
        primaryStage.setTitle("Random Moving Ghosts");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void moveGhost(ImageView ghost, Random random) {
        // Generate a random direction
        int direction = random.nextInt(4); // 0 = up, 1 = down, 2 = left, 3 = right
        double newX = ghost.getX();
        double newY = ghost.getY();

        switch (direction) {
            case 0 -> newY -= STEP; // Move up
            case 1 -> newY += STEP; // Move down
            case 2 -> newX -= STEP; // Move left
            case 3 -> newX += STEP; // Move right
        }

        // Ensure the ghost stays within bounds
        newX = Math.max(0, Math.min(newX, SCENE_WIDTH - ghost.getFitWidth()));
        newY = Math.max(0, Math.min(newY, SCENE_HEIGHT - ghost.getFitHeight()));

        // Update ghost position
        ghost.setX(newX);
        ghost.setY(newY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

