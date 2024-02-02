package com.fallingsand;

public class Grid {
    private int width;
    private int height;
    private int[][] grid;

    public void initialize(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[height][width];
    }

    // Allow us to clear the canvas
    public void clear() {
        this.grid = new int[height][width];
    }

    // Check if a particle exists in a space
    public boolean isEmpty(int x, int y) {
        return this.grid[x][y] == 0;
    }
}

