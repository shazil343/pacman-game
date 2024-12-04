package com.example.groupproject16;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class HumanPlayer {

    private static final int TILE_SIZE = 10;
    private int row;
    private int col;
    private final ImageView pacMan;

    public HumanPlayer(int startRow, int startCol) {
        this.row = startRow;
        this.col = startCol;

        // Initialize Pac-Man
        Image pacManImage = new Image(getClass().getResource("/Images/moving-pacman.gif").toExternalForm());
        pacMan = new ImageView(pacManImage);
        pacMan.setFitWidth(TILE_SIZE);
        pacMan.setFitHeight(TILE_SIZE);
        pacMan.setX(col * TILE_SIZE);
        pacMan.setY(row * TILE_SIZE);
    }

    public ImageView getPacMan() {
        return pacMan;
    }

    public void move(KeyCode direction, Maze maze) {
        int nextRow = row;
        int nextCol = col;

        switch (direction) {
            case UP -> nextRow--;
            case DOWN -> nextRow++;
            case LEFT -> nextCol--;
            case RIGHT -> nextCol++;
        }

        if (maze.isValidMove(nextRow, nextCol)) {
            row = nextRow;
            col = nextCol;
            pacMan.setX(col * TILE_SIZE);
            pacMan.setY(row * TILE_SIZE);
        }
    }
}
