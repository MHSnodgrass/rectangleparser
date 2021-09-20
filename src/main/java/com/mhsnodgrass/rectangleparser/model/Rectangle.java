package com.mhsnodgrass.rectangleparser.model;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** Represents a Rectangle.
 * @author Matthew Snodgrass
 */
public class Rectangle {
    // enums
    public enum Adjacency {
        SUBLINE,
        PROPER,
        PARTIAL,
        NONE
    }

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
     * <p>Creates a Rectangle with a specified id and height, width, and the x, y coordinates for the top left of the Rectangle</p>
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
     * <p>This method checks for intersection and then returns the intersection coordinates</p>
     * <p>Checks for intersection first using {@Link doesIntersect}</p>
     * <p>Does include coordinates for a perfect overlap, and overlapping lines when a positive intersection is also present</p>
     * @param rect2 Rectangle sent in to see if it intersects with this Rectangle
     * @return A list of lists of x, y pair values to represent the intersection points
     */
    public List<Pair<Integer, Integer>> getIntersect(Rectangle rect2) {
        List<Pair<Integer, Integer>> results = new ArrayList<>();
        List<Pair<Integer, Integer>> firstCoordinates = this.getAllCoordinates().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<Pair<Integer, Integer>> secondCoordinates = rect2.getAllCoordinates().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // Checks if there is an intersection first
        if (doesIntersect(rect2)) {
            // Filter both lists into one by checking if a value is present in both
            results = firstCoordinates;
            if (results.retainAll(secondCoordinates)) {
                return results;
            } else {
                results = null;
            }
        }

        return results;
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
        if (doesRectanglesOverlap(rect2)) {
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

    /**
     * <p>This method checks for adjacency, including sub-line, proper, and partial</p>
     * <p>Uses all of the coordinates of each Rectangle</p>
     * <p>Checks if a Rectangle has all of the coordinates of another Rectangle for proper and sub-line</p>
     * <p>Checks if a Rectangle has any of the coordinates of another Rectangle for partial</p>
     * @param rect2 Rectangle sent in to see if this Rectangle is adjacent
     * @return Adjacency (enum) value to represent what, if any, adjacency is present (PROPER, SUBLINE, PARTIAL, NONE)
     */
    public Adjacency isAdjacent(Rectangle rect2) {
        List<List<Pair<Integer, Integer>>> firstCoordinates = this.getAllCoordinates();
        List<List<Pair<Integer, Integer>>> secondCoordinates = rect2.getAllCoordinates();
        Adjacency adjacency = Adjacency.NONE;

        // Check for perfect overlap, skip if true
        if (!doesRectanglesOverlap(rect2)) {
            // Check for PROPER or SUBLINE first
            // Check each side of the first Rectangle
            for (int i = 0; i < firstCoordinates.size(); i++) {
                // Check each side of the second Rectangle against each side of the first
                for (int j = 0; j < secondCoordinates.size(); j++) {
                    // Check if all coordinates match
                    Boolean allMatchOne = firstCoordinates.get(i).containsAll(secondCoordinates.get(j));
                    Boolean allMatchTwo = secondCoordinates.get(j).containsAll(firstCoordinates.get(i));
                    if (allMatchOne || allMatchTwo) {
                        // Check if the amount of coordinates is the same
                        if (firstCoordinates.get(i).size() == secondCoordinates.get(j).size()) {
                            return Adjacency.PROPER;
                        } else {
                            return Adjacency.SUBLINE;
                        }
                    }
                }
            }

            // Check if it is an intersection first, skip if true
            if (!doesIntersect(rect2)) {
                // Check again for Partial if PROPER or SUBLINE was not found
                for (int i = 0; i < firstCoordinates.size(); i++) {
                    // Check each side of the second Rectangle against each side of the first
                    for (int j = 0; j < secondCoordinates.size(); j++) {
                        // Check if any of the coordinates match
                        Boolean anyMatch = CollectionUtils.containsAny(firstCoordinates.get(i), secondCoordinates.get(j));
                        if (anyMatch) {
                            return Adjacency.PARTIAL;
                        }
                    }
                }
            }
        }

        return adjacency;
    }

    /**
     * Gets all of the Rectangle's coordinates
     * @return A List containing Lists of pairs of coordinates for the top, right, bottom, and left of the Rectangle
     */
    public List<List<Pair<Integer, Integer>>> getAllCoordinates() {
        List<List<Pair<Integer, Integer>>> results = new ArrayList<>();

        // Top Coordinates
        results.add(getCoordinateRange(0, 1));
        // Right Coordinates
        results.add(getCoordinateRange(3, 1));
        // Bottom Coordinates
        results.add(getCoordinateRange(2, 3));
        // Left Coordinates
        results.add(getCoordinateRange(2, 0));

        return results;
    }

    // Helper functions
    // Grab the coordinates and return them as a string
    private String getCoordinatesFromList(List<List<Integer>> coordinates, int index) {
        String x = coordinates.get(index).get(0).toString();
        String y = coordinates.get(index).get(1).toString();
        return x + ", " + y;
    }

    // Grab the range of coordinates between two coordinates
    private List<Pair<Integer, Integer>> getCoordinateRange(Integer firstCoordinate, Integer secondCoordinate) {
        List<Pair<Integer, Integer>> coordinates = new ArrayList<>();
        List<Integer> xCoordinates = new ArrayList<>();
        List<Integer> yCoordinates = new ArrayList<>();
        Integer x1;
        Integer y1;
        Integer x2;
        Integer y2;

        x1 = this.coordinates.get(firstCoordinate).get(0);
        y1 = this.coordinates.get(firstCoordinate).get(1);
        x2 = this.coordinates.get(secondCoordinate).get(0);
        y2 = this.coordinates.get(secondCoordinate).get(1);

        // Check for a static coordinate between both points
        if (x1 == x2) {
            xCoordinates.add(x1);
        } else {
            xCoordinates = IntStream.range(x1, x2 + 1).boxed().collect(Collectors.toList());
        }

        if (y1 == y2) {
            yCoordinates.add(y1);
        } else {
            yCoordinates = IntStream.range(y1, y2 + 1).boxed().collect(Collectors.toList());
        }

        // Check if a list needs to be populated
        if (xCoordinates.size() == 1) {
            List<Integer> tempList = new ArrayList<>();
            for (int i = 0; i < yCoordinates.size(); i++) {
                tempList.add(xCoordinates.get(0));
            }
            xCoordinates = tempList;
        }

        if (yCoordinates.size() == 1) {
            List<Integer> tempList = new ArrayList<>();
            for (int i = 0; i < xCoordinates.size(); i++) {
                tempList.add(yCoordinates.get(0));
            }
            yCoordinates = tempList;
        }

        // Create the pairs and add to the list
        for (int i = 0; i < xCoordinates.size(); i++) {
            ImmutablePair<Integer, Integer> p = new ImmutablePair<>(xCoordinates.get(i), yCoordinates.get(i));
            coordinates.add(p);
        }

        return coordinates;
    }

    public Boolean doesRectanglesOverlap(Rectangle rect2) {
        return (this.height == rect2.getHeight() && this.width == rect2.getWidth() && this.x == rect2.getX() && this.y == rect2.getY());
    }
}
