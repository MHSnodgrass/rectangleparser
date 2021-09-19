package com.mhsnodgrass.rectangleparser.model;

import java.util.ArrayList;
import java.util.List;

/** Represents a Rectangle.
 * @author Matthew Snodgrass
 */
public class Rectangle {
    // Fields
    // All fields are final, this class is not meant to do transform methods
    private final Integer id;
    private final Integer height;
    private final Integer width;

    // Both x and y represent the top left coordinate
    private final Integer x;
    private final Integer y;

    // List of coordinates are created using the first coordinate and the height and width
    // The list will contain 4 sets of x and y coordinates of the rectangle.
    // Index listing: (0 - Top left, 1 - Top Right, 2 - Bottom Left, 3 - Bottom Right)
    private final List<List<Integer>> coordinates = new ArrayList<>();

    // Constructor
    /**
     * <p>Creates a Rectangle with a specified height, width, and the x, y coordinates for the top left of the Rectangle</p>
     * <p>Also creates the coordinate list (top left, top right, bottom left, bottom right) using the width and height</p>
     * @param id An Integer representing a unique Rectangle
     * @param height An Integer representing the height of the Rectangle
     * @param width An Integer representing the width of the Rectangle
     * @param x An Integer representing the top left x coordinate of the Rectangle
     * @param y An Integer representing the top left y coordinate of the Rectangle
     */
    public Rectangle (Integer id, Integer height, Integer width, Integer x, Integer y) {
        this.id = id;
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

    /**
     * Gets the Rectangle's id
     * @return An Integer representing a unique Rectangle
     */
    public Integer getId() {
        return id;
    }
    /**
     * Gets the Rectangle's height
     * @return An Integer representing the Rectangle's height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Gets the Rectangle's width
     * @return An Integer representing the Rectangle's width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Gets the Rectangle's top left x coordinate
     * @return An Integer representing the Rectangle's top left x coordinate
     */
    public Integer getX() {
        return x;
    }

    /**
     * Gets the Rectangle's top left y coordinate
     * @return An Integer representing the Rectangle's top left y coordinate
     */
    public Integer getY() {
        return y;
    }

    /**
     * Gets the Rectangle's top left, top right, bottom left, and bottom right coordinates
     * @return A List containing Lists of x and y coordinates
     */
    public List<List<Integer>> getCoordinates() {
        return coordinates;
    }

    // Methods

    /**
     * toString override
     * @return A string representing the rectangle, including all of the coordinates
     */
    @Override
    public String toString() {
         return "ID: " + this.id + ", " +
                 "WIDTH: " + this.width + ", " +
                 "HEIGHT: " + this.height + " | " +
                 "COORDINATES: " +
                 "TL: " + "(" + getCoordinatesFromList(coordinates, 0) + ")" + " / " +
                 "TR: " + "(" + getCoordinatesFromList(coordinates, 1) + ")" + " / " +
                 "BL: " + "(" + getCoordinatesFromList(coordinates, 2) + ")" + " / " +
                 "BR: " + "(" + getCoordinatesFromList(coordinates, 3) + ")";
    }

    /**
     * <p>This method checks for intersection, including perfect overlap</p>
     * <p>Uses the bottom left and top right coordinate of each rectangle to determine</p>
     * <p>Checks if the following is true:</p>
     * <p>X2R, Y2R > X1L, Y1L</p>
     * <p>X1R, Y1R > X2L, Y2L</p>
     * <p>X (Coordinate) - 1 (Rectangle Number) - R (Coordination Position, R would be Top Right)</p>
     * @param rect2 Rectangle sent in to see if it intersects with this Rectangle
     * @return Boolean value to represent if an intersection is present
     */
    public Boolean doesIntersect(Rectangle rect2) {
        // First Rectangle
        Integer x1L = this.coordinates.get(2).get(0);
        Integer y1L = this.coordinates.get(2).get(1);
        Integer x1R = this.coordinates.get(1).get(0);
        Integer y1R = this.coordinates.get(1).get(1);

        // Second Rectangle
        Integer x2L = rect2.getCoordinates().get(2).get(0);
        Integer y2L = rect2.getCoordinates().get(2).get(1);
        Integer x2R = rect2.getCoordinates().get(1).get(0);
        Integer y2R = rect2.getCoordinates().get(1).get(1);

        // Check for perfect overlap
        if (x1L == x2L && y1L == y2L && x1R == x2R && y1R == y2R) {
            return true;
        }

        // Check for regular intersection
        if ((x2R > x1L && y2R > y1L) && (x1R > x2L && y1R > y2L)) {
            return true;
        }

        // Return false by default if nothing else matches
        return false;
    }

    /**
     * <p>This method checks if this Rectangle contains the Rectangle sent in</p>
     * <p>Uses the bottom left and top right coordinate of each rectangle to determine</p>
     * <p>Checks if the following is true:</p>
     * <p>X1L, Y1L < X2L, Y2L</p>
     * <p>X1R, Y1R > X2L, Y2L</p>
     * <p>X (Coordinate) - 1 (Rectangle Number) - R (Coordination Position, R would be Top Right)</p>
     * @param rect2 Rectangle sent in to see if this Rectangle contains it
     * @return Boolean value to represent if there is containment
     */
    public Boolean doesContain(Rectangle rect2) {
        // First Rectangle
        Integer x1L = this.coordinates.get(2).get(0);
        Integer y1L = this.coordinates.get(2).get(1);
        Integer x1R = this.coordinates.get(1).get(0);
        Integer y1R = this.coordinates.get(1).get(1);

        // Second Rectangle
        Integer x2L = rect2.getCoordinates().get(2).get(0);
        Integer y2L = rect2.getCoordinates().get(2).get(1);
        Integer x2R = rect2.getCoordinates().get(1).get(0);
        Integer y2R = rect2.getCoordinates().get(1).get(1);

        // Check for containment
        if (x1L < x2L && y1L < y2L && x1R > x2R && y1R > y2R) {
            return true;
        }

        // Return false by default if nothing matches
        return false;
    }

    // Helper functions
    // Grab the coordinates and return them as a string
    private String getCoordinatesFromList(List<List<Integer>> coordinates, int index) {
        String x = coordinates.get(index).get(0).toString();
        String y = coordinates.get(index).get(1).toString();
        return x + ", " + y;
    }
}
