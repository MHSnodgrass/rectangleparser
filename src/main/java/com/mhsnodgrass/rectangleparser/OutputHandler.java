package com.mhsnodgrass.rectangleparser;

import com.mhsnodgrass.rectangleparser.model.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/** OutputHandler handles the options accepted by the {@link CommandRunner}
 * @author Matthew Snodgrass
 */
@Slf4j
@Component
public class OutputHandler {
    @Value("${rectangleparser.default.filename}")
    private String filename;

    private Integer idOne;
    private Integer idTwo;

    @Autowired
    private RectangleParser rectangleParser;

    /**
     * <p>Parses the CommandLine input sent in and checks for any filenames, will use default from application.properties if not found</p>
     * <p>If the filename is missing '.xml', it will add it</p>
     * <p>Will send the filename to {@link RectangleParser} to have the file created and retrieve the Rectangle objects</p>
     * <p>Will output each Rectangle using it's toString method</p>
     * @param cmd Commandline contains any arguments from the user for changing what file should be read in
     * @param verbose Boolean value that decides if all the coordinates for the Rectangle should be printed
     */
    public void printRectangleList(CommandLine cmd, Boolean verbose) {
        // Check arguments
        processArgs(cmd, true);
        // Output Rectangles
        outputRectangleInfo(rectangleParser.getRectangleListFromFile(filename), verbose);
    }

    /**
     * <p>Parses the CommandLine input sent in and checks for filename, id for the first rectangle, and the id for the second rectangle</p>
     * <p>Each argument is required. If the filename is missing '.xml', it will add it</p>
     * <p>Will send the filename to {@link RectangleParser} to have the file created and retrieve the Rectangle objects</p>
     * <p>Will output each Rectangle using it's toString method, tell the user if they intersect, and print out any intersecting coordinates</p>
     * @param cmd Commandline contains arguments for the file to be read in and an id for the two rectangles that are to be checked
     */
    public void printIntersectionCoordinates(CommandLine cmd, Boolean verbose) {
        // Check arguments
        processArgs(cmd, false);

        // Grab the list of files
        List<Rectangle> rectangleList = rectangleParser.getRectangleListFromFile(filename);

        // Filter for rectangles matching ids given by user
        List<Rectangle> rect = rectangleParser.filterRectanglesListByIds(rectangleList, idOne, idTwo);

        // Check list before processing
        if (rect != null) {
            // Get the Intersect Values
            List<Pair<Integer, Integer>> intersectValues = rectangleParser.intersect(rect);
            outputRectangleInfo(rect, verbose);
            log.info("--------------------");
            log.info("DOES RECTANGLE #2 INTERSECT RECTANGLE #1: " + ((intersectValues != null) ? "Yes" : "No"));

            if (intersectValues != null) {
                String values = "";
                for (Pair<Integer, Integer> p : intersectValues) {
                    values += "(" + p.getKey() + ", " + p.getValue() + ")" + " | ";
                }
                if (!values.isEmpty()) {
                    values = values.substring(0, values.length() - 2);
                }
                log.info("INTERSECTING COORDINATES: " + values);
            }
        }
    }

    /**
     * <p>Parses the CommandLine input sent in and checks for filename, id for the first rectangle, and the id for the second rectangle</p>
     * <p>Each argument is required. If the filename is missing '.xml', it will add it</p>
     * <p>Will send the filename to {@link RectangleParser}  to have the file created and retrieve the Rectangle objects</p>
     * <p>Will output each Rectangle using it's toString method and tell the user if the first rectangle contains the second</p>
     * @param cmd Commandline contains arguments for the file to be read in and an id for the two rectangles that are to be checked
     */
    public void printContainment(CommandLine cmd, Boolean verbose) {
        // Check arguments
        processArgs(cmd, false);

        // Grab the list of files
        List<Rectangle> rectangleList = rectangleParser.getRectangleListFromFile(filename);

        // Filter for rectangles matching ids given by user
        List<Rectangle> rect = rectangleParser.filterRectanglesListByIds(rectangleList, idOne, idTwo);

        // Check list before processing
        if (rect != null) {
            Boolean contain = rectangleParser.contain(rect);
            outputRectangleInfo(rect, verbose);
            log.info("--------------------");
            log.info("DOES RECTANGLE #1 CONTAIN RECTANGLE #2: " + ((contain) ? "Yes" : "No"));
        }
    }

    /**
     * <p>Parses the CommandLine input sent in and checks for filename, id for the first rectangle, and the id for the second rectangle</p>
     * <p>Each argument is required. If the filename is missing '.xml', it will add it</p>
     * <p>Will send the filename to {@link RectangleParser}  to have the file created and retrieve the Rectangle objects</p>
     * <p>Will check for a rectangle for each id, and will check if they are adjacent</p>
     * <p>Will output each Rectangle using it's toString method and tell the user if they are adjacent and what type of adjacency is present</p>
     * @param cmd Commandline contains arguments for the file to be read in and an id for the two rectangles that are to be checked
     */
    public void printAdjacency(CommandLine cmd, Boolean verbose) {
        // Check arguments
        processArgs(cmd, false);

        // Grab the list of files
        List<Rectangle> rectangleList = rectangleParser.getRectangleListFromFile(filename);

        // Filter for rectangles matching ids given by user
        List<Rectangle> rect = rectangleParser.filterRectanglesListByIds(rectangleList, idOne, idTwo);

        // Check list before processing
        if (rect != null) {
            Rectangle.Adjacency adjacency = rectangleParser.adjacent(rect);
            outputRectangleInfo(rect, verbose);
            log.info("--------------------");
            log.info("IS RECTANGLE #1 & RECTANGLE #2 ADJACENT:  " + ((adjacency == Rectangle.Adjacency.NONE) ? "No" : "Yes"));
            log.info("ADJACENT TYPE: " + returnStringFromEnum(adjacency));
        }
    }

    // Helper Methods
    private void outputRectangleInfo(List<Rectangle> rectangleList, Boolean verbose) {
        for (int i = 0; i < rectangleList.size(); i++) {
            log.info("--------------------");
            log.info("RECTANGLE #" + (i + 1));
            log.info(rectangleList.get(i).toString());
            if (verbose) {
                log.info("--------------------");
                log.info("Top Coordinates:");
                log.info("--------------------");
                for (Pair<Integer, Integer> p : rectangleList.get(i).getAllCoordinates().get(0)) {
                    log.info("(" + p.getKey() + ", " + p.getValue() + ")");
                }
                log.info("--------------------");
                log.info("Right Coordinates:");
                log.info("--------------------");
                for (Pair<Integer, Integer> p : rectangleList.get(i).getAllCoordinates().get(1)) {
                    log.info("(" + p.getKey() + ", " + p.getValue() + ")");
                }
                log.info("--------------------");
                log.info("Bottom Coordinates:");
                log.info("--------------------");
                for (Pair<Integer, Integer> p : rectangleList.get(i).getAllCoordinates().get(2)) {
                    log.info("(" + p.getKey() + ", " + p.getValue() + ")");
                }
                log.info("--------------------");
                log.info("Left Coordinates:");
                log.info("--------------------");
                for (Pair<Integer, Integer> p : rectangleList.get(i).getAllCoordinates().get(3)) {
                    log.info("(" + p.getKey() + ", " + p.getValue() + ")");
                }
            }
        }
    }

    private void checkFilenameExtension(String fn) {
        filename = fn;
        filename = filename.endsWith(".xml") ? filename : filename + ".xml";
    }

    private void processArgs(CommandLine cmd, Boolean regularParse) {
        if (regularParse) {
            // Check if user provided name of XML file, if not, process default
            if (!cmd.getArgList().isEmpty() && cmd.getArgList().size() == 1) {
                checkFilenameExtension(cmd.getArgList().get(0));
            } else {
                log.info("Filename was not provided, using default: " + filename);
            }
        } else {
            if (!cmd.getArgList().isEmpty() && cmd.getArgList().size() == 3) {
                try {
                    idOne = Integer.parseInt(cmd.getArgList().get(1));
                    idTwo = Integer.parseInt(cmd.getArgList().get(2));
                } catch (NumberFormatException e) {
                    log.error("Error parsing one of the ids passed in, please make sure the id is a number", e);
                }
            } else {
                log.error("The number of arguments is not 3. Please send in <filename> <id> <id>");
            }
        }
    }

    private String returnStringFromEnum(Rectangle.Adjacency adjacency) {
        if (adjacency == Rectangle.Adjacency.PROPER) {
            return "Proper";
        } else if (adjacency == Rectangle.Adjacency.SUBLINE) {
            return "Sub-Line";
        } else if (adjacency == Rectangle.Adjacency.PARTIAL) {
            return "Partial";
        } else {
            return "None";
        }
    }
}
