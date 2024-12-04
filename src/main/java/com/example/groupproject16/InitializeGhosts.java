package com.example.groupproject16;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InitializeGhosts {

    public static final int GHOST_COUNT = 4; // Make GHOST_COUNT static so it can be accessed from Maze class

    private static final String[] ghostImages = {
            "green-ghost.gif", "orange-ghost.gif", "pink-ghost.gif", "red-ghost.gif"
    };

    public static ImageView[] createGhosts(int count, int tileSize) {
        ImageView[] ghosts = new ImageView[count];
        for (int i = 0; i < count; i++) {
            Image ghostImage = new Image(
                    InitializeGhosts.class.getResource("/Images/" + ghostImages[i]).toExternalForm()
            );
            ImageView ghost = new ImageView(ghostImage);
            ghost.setFitWidth(tileSize * 2.5);
            ghost.setFitHeight(tileSize * 2.5);
            ghosts[i] = ghost;
        }
        return ghosts;
    }
}



