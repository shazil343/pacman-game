
package com.example.groupproject16;

import javafx.application.Application;
import javafx.stage.Stage;

public class PacManGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create an instance of StartScreen
        StartScreen startScreen = new StartScreen();

        // Set up the action for transitioning to the Maze
        startScreen.setStartGameAction(() -> initializeMaze(primaryStage));

        // Launch the start screen
        startScreen.start(primaryStage);
    }

    private void initializeMaze(Stage primaryStage) {
        // Create an instance of Maze
        Maze maze = new Maze();

        // Start the Maze class in the same stage
        maze.start(primaryStage); //call method
    }



    public static void main(String[] args) {
        launch(args);
    }
}


