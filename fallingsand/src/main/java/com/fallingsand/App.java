package com.fallingsand;

import javafx.animation.AnimationTimer;
//imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application{
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    int cellSize = 30;
    int cols=(int)WIDTH/cellSize;
    int rows=(int)HEIGHT/cellSize;
    boolean[][] cells = new boolean[rows][cols];

   /*start method is the starting point of the application
   primary stage is the main window of the application*/
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Falling Sand Game");
        //create a canvas
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        //get the graphics context which allows usto draw on the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //draw the grid
        drawGrid(gc);

        //set the canvas events like mouse drag and mouse press
        setCanvasEvents(canvas, gc);

        /*StackPane is a container class in JavaFX. 
        It lays out its children in a back-to-front stack (z-axis)*/
        StackPane root = new StackPane();
        /*The getChildren() method returns the list of children of this StackPane
        the add() method appends the canvas to this list*/
        root.getChildren().add(canvas);

        /*add everything we created so far to the application window
        by creating a Scene object and setting it to the primary stage*/
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        for (int x = 0; x <= WIDTH; x += cellSize) {
            gc.strokeLine(x, 0, x, HEIGHT);
        }
        for (int y = 0; y <= HEIGHT; y += cellSize) {
            gc.strokeLine(0, y, WIDTH, y);
        }
    } 

    private void setCanvasEvents(Canvas canvas, GraphicsContext gc) {
        canvas.setOnMouseClicked(event -> {
            int cellX = (int) (event.getX() / cellSize);
            int cellY = (int) (event.getY() / cellSize);
            cells[cellX][cellY] = true;
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Clear the canvas
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Update and redraw cells
                for (int x = 0; x < cells.length; x++) {
                    for (int y = cells[0].length - 1; y >= 0; y--) {
                        if (cells[x][y]) {
                            // If the cell below this one is empty, move this cell down
                            if (y < cells[0].length - 1 && !cells[x][y + 1]) {
                                cells[x][y] = false;
                                cells[x][y + 1] = true;
                            }

                            // Draw the cell
                            gc.setFill(Color.BLACK);
                            gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                        }
                    }
                }

                // Redraw the grid
                drawGrid(gc);
            }
        }.start();

    }

    public static void main(String[] args) {
        launch();
    }
}
