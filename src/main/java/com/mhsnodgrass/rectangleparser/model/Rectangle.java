package com.mhsnodgrass.rectangleparser.model;

import java.util.ArrayList;
import java.util.List;

public class Rectangle {
    // Fields
    // All fields are final, this class is not meant to do transform methods
    private final Integer height;
    private final Integer width;

    // Both x and y represent the top left coordinate
    private final Integer x;
    private final Integer y;

    // List of coordinates are created using the first coordinate and the height and width
    // The list will contain 4 sets of x and y coordinates of the rectangle.
    // Index listing: (0 - Top left, 1 - Top Right, 2 - Bottom Left, 3 - Bottom Right)
    private List<List<Integer>> coordinates = new ArrayList<>();

    // Constructor
    public Rectangle (Integer height, Integer width, Integer x, Integer y) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;

        // First set of coordinates (top left)
        List<Integer> coordinateSet = new ArrayList<>();
        coordinateSet.add(x);
        coordinateSet.add(y);
        coordinates.add(coordinateSet);
        coordinateSet = new ArrayList<>();

        // Second set of coordinates (top right)
        Integer newX = x + width;
        coordinateSet.add(newX);
        coordinateSet.add(y);
        coordinates.add(coordinateSet);
        coordinateSet = new ArrayList<>();

        // Third set of coordinates (bottom left)
        Integer newY = y - height;
        coordinateSet.add(x);
        coordinateSet.add(newY);
        coordinates.add(coordinateSet);
        coordinateSet = new ArrayList<>();

        // Fourth set of coordinates (bottom right)
        coordinateSet.add(newX);
        coordinateSet.add(newY);
        coordinates.add(coordinateSet);
    }

    // Getters
    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public List<List<Integer>> getCoordinates() {
        return coordinates;
    }
}
