package com.mhsnodgrass.rectangleparser.model;

import java.util.ArrayList;
import java.util.HashMap;
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
    // The list will contain 4 sets of x and y coordinate of the rectangle.
    // Index listing: (0 - Top left, 1 - Top Right, 2 - Bottom Left, 3 - Bottom Right)
    private final List<HashMap<Integer, Integer>> coordinates = new ArrayList<>();

    // Constructor
    public Rectangle (Integer height, Integer width, Integer x, Integer y) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;

        // First set of coordinates (top left)
        HashMap<Integer, Integer> coordinateSet = new HashMap<>();
        coordinateSet.put(x, y);
        coordinates.add(coordinateSet);
        coordinateSet.clear();

        // Second set of coordinates (top right)
        Integer newX = x + width;
        coordinateSet.put(newX, y);
        coordinates.add(coordinateSet);
        coordinateSet.clear();

        // Third set of coordinates (bottom left)
        Integer newY = y - height;
        coordinateSet.put(x, newY);
        coordinates.add(coordinateSet);
        coordinateSet.clear();

        // Fourth set of coordinates (bottom right)
        coordinateSet.put(newX, newY);
        coordinates.add(coordinateSet);
        coordinateSet.clear();
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

    public List<HashMap<Integer, Integer>> getCoordinates() {
        return coordinates;
    }
}
