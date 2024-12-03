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

    //should hold the actual positioning of pacman i.e logic not fully implemented yet
    public static double pacManX = 300;
    public static double pacManY = 200;

    public void start(Stage primaryStage) {
        // Create a Pane to hold the ghosts
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        // Random number generator for ghost directions
        Random random = new Random();

        // Array to hold file paths for ghost GIFs
        String[] ghostImages = {"green-ghost.gif", "orange-ghost.gif", "pink-ghost.gif", "red-ghost.gif"};

        // Array to hold ghost ImageViews
        ImageView[] ghosts = new ImageView[GHOST_COUNT];

        // Initialize each ghost
        for (int i = 0; i < GHOST_COUNT; i++) {
            // Load the ghost GIF
            Image ghostImage = new Image(getClass().getResource("/Images/" + ghostImages[i]).toExternalForm());
            ImageView ghost = new ImageView(ghostImage);

            // Set initial position and size for each ghost
            ghost.setFitWidth(40);
            ghost.setFitHeight(40);
            ghost.setX(random.nextInt(SCENE_WIDTH - 40)); // Random initial X position
            ghost.setY(random.nextInt(SCENE_HEIGHT - 40)); // Random initial Y position

            // Add ghost to the array and the Pane
            ghosts[i] = ghost;
            root.getChildren().add(ghost);
        }

        // Create a Timeline for moving ghosts
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            for (ImageView ghost : ghosts) {
                moveGhost(ghost);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Create the scene
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        // Set up the stage
        primaryStage.setTitle("Moving Ghosts Towards Pac-Man");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void moveGhost(ImageView ghost) {
        // Calculate the direction to move based on Pac-Man's position
        double ghostX = ghost.getX();
        double ghostY = ghost.getY();
        double deltaX = pacManX - ghostX;
        double deltaY = pacManY - ghostY;

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            ghost.setX(ghostX + Math.signum(deltaX) * STEP); // Move in x direction
        } else {
            ghost.setY(ghostY + Math.signum(deltaY) * STEP); // Move in y direction
        }

        // Ensure the ghost stays within bounds
        ghost.setX(Math.max(0, Math.min(ghost.getX(), SCENE_WIDTH - ghost.getFitWidth())));
        ghost.setY(Math.max(0, Math.min(ghost.getY(), SCENE_HEIGHT - ghost.getFitHeight())));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
