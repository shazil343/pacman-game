package com.example.groupproject16;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PacManGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create an instance of StartScreen
        StartScreen startScreen = new StartScreen();

        // Launch the start screen
        startScreen.start(primaryStage);



    }

    public static void main(String[] args) {
        launch(args);
    }
}
