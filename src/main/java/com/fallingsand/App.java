package com.fallingsand;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application{
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    int cellSize = 10;
    int cols=(int)WIDTH/cellSize;
    int rows=(int)HEIGHT/cellSize;
    int[][] grid = new int[rows][cols];
    private long counter = 0;
    private int colorIndex = 1;

   /*start method is the starting point of the application
   primary stage is the main window of the application*/
    @Override
    public void start(Stage primaryStage) {
        //create a canvas
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        //get the graphics context which allows usto draw on the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //main logic that draws the sand
        draw(canvas, gc);

        /*StackPane is a container class in JavaFX. 
        It lays out its children in a back-to-front stack (z-axis)*/
        StackPane root = new StackPane();
        /*The getChildren() method returns the list of children of this StackPane
        the add() method appends the canvas to this list*/
        root.getChildren().add(canvas);
        
        // set the title of the window
        primaryStage.setTitle("Falling Sand Game");
        
        // set the icon for the window
        primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("fsg_icon.png")));

        /*add everything we created so far to the application window
        by creating a Scene object and setting it to the primary stage*/
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));

        primaryStage.show();
    }

    private void draw(Canvas canvas, GraphicsContext gc) {
        canvas.setOnMouseDragged(event -> {
            double mouseX=event.getX();//get the horizontal pixel position of the mouse
            double mouseY=event.getY();//get the vertical pixel position of the mouse

        /*  Our windows is 600x600 and we have 20x20 cells with each cell being 30x30 pixel
            Now, when the user clicks on any pixel, we need to find out which cell the user clicked on
            So, we divide the pixel position by the cell size to get which cell our pixel lies in, and then floor the value to get the cell number
            For example, if the user clicks on pixel (100,100) then the cell number would be (100/30, 100/30)=(3,3). So, the user clicked on cell number (3,3)
            When its time to actually fill the cell, we will multiply the cell number by the cell size, this gives us the top left corner of the cell
            aka the origin of the cell. So, the cell number (3,3) would be (3*30,3*30)=(90,90) which is the top left corner of the cell
            and we will place a 30x30 black square using fillRect() method */

            //if the mouse is within the canvas update the grid
            if(mouseX>=0 && mouseX<WIDTH && mouseY>=0 && mouseY<HEIGHT){
                int cellX = (int) (mouseX / cellSize);
                int cellY = (int) (mouseY / cellSize);
                grid[cellX][cellY] = colorIndex;
            }
        });

        canvas.setOnMousePressed(event -> {
            double mouseX=event.getX();//get the horizontal pixel position of the mouse
            double mouseY=event.getY();//get the vertical pixel position of the mouse
            if(mouseX>=0 && mouseX<WIDTH && mouseY>=0 && mouseY<HEIGHT){
                int cellX = (int) (mouseX / cellSize);
                int cellY = (int) (mouseY / cellSize);
                grid[cellX][cellY] = colorIndex;
            }
        });
        //a color array to hold all 360 possible hues at maximum saturation and brightness
        Color[] colors = new Color[360];
        for (int i = 1; i < colors.length; i++) {
            colors[i] = Color.hsb(i, 1.0, 1.0);
        }

        new AnimationTimer() { //this is a loop that runs every frame
            @Override
            public void handle(long now) {
                //increment counter every frame
                counter++;
                
                // Change color based on the counter, we can change rate however we want
                if (counter % 10 == 0) {
                    colorIndex = (colorIndex + 1) % colors.length;
                }
                // Clear the canvas, so that we can draw the updated grid (think of crt monitors and how they draw pixels on the screen)
                gc.clearRect(0, 0, WIDTH, HEIGHT);

                //outer loop iterates through the columns from left to right
                for (int x = 0; x < cols; x++) {
                    //inner loop iterates through the rows in from bottom to top(why? the origin is at the top left corner of the screen, so the y axis is inverted)
                    for (int y = rows-1; y >= 0; y--) {
                        //if the sand particle is present, first we will draw it and then update its position
                        if (grid[x][y]>0) {
                            //draw the current sand particle
                            gc.setFill(colors[grid[x][y]]);
                            gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);

                            // if sand is in the air (i.e cell below current sand particle is empty) then just move it down
                            if (y < rows - 1 && grid[x][y + 1]==0) {
                                    swap(x, y, x, y+1);
                            }
                            //if cell below current sand particle is filled, then check -
                            // if the bottom left and right cell both are empty then randomly move the sand to one of them
                            else if(x<cols-1 && y<rows-1 && x>0 && y<rows-1 && grid[x+1][y+1]==0 && grid[x-1][y+1]==0){
                                int direction=(Math.random()*1>0.5)?1:-1;
                                swap(x, y, x+direction, y+1);   
                            }
                            //if only the bottom right cell is empty then move the sand to that cell
                            else if(x<cols-1 && y<rows-1 && grid[x+1][y+1]==0){
                                swap(x, y, x+1, y+1);
                            }
                            //if only the bottom left cell is empty then move the sand to that cell
                            else if(x>0 && y<rows-1 && grid[x-1][y+1]==0){
                                swap(x, y, x-1, y+1);
                            }
                        }
                    }
                }
            }
        }.start();
    }

    public void swap(int x1, int y1, int x2, int y2){
        int temp=grid[x1][y1];
        grid[x1][y1]=grid[x2][y2];
        grid[x2][y2]=temp;
    }

    public static void main(String[] args) {
        launch();
    }
}
