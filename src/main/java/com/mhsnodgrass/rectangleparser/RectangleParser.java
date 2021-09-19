package com.mhsnodgrass.rectangleparser;

import com.mhsnodgrass.rectangleparser.model.Rectangle;
import com.mhsnodgrass.rectangleparser.util.RectangleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** RectangleParser handles the options accepted by the {@link CommandRunner}
 * @author Matthew Snodgrass
 */
@Slf4j
@Component
public class RectangleParser {
    @Autowired
    private RectangleUtils rectangleUtils;

    @Value("${rectangleparser.default.filename}")
    private String filename;

    /**
     * <p>Parses the CommandLine input sent in and checks for any filenames, will use default from application.properties if not found</p>
     * <p>If the filename is missing '.xml', it will add it</p>
     * <p>It will create the file from the filename and send it to {@link RectangleUtils} to retrieve the Rectangle objects</p>
     * <p>Will output each Rectangle using it's toString method</p>
     * @param cmd Commandline contains any arguments from the user for changing what file should be read in
     */
    public void parse(CommandLine cmd, Boolean verbose) {
        // Check if user provided name of XML file, if not, process default
        if (!cmd.getArgList().isEmpty() && cmd.getArgList().size() == 1) {
            checkFilenameExtension(cmd.getArgList().get(0));
        } else {
            log.info("Filename was not provided, using default: " + filename);
        }

        // Grab list of rectangles
        List<Rectangle> rectangleList = getRectangleListFromFile();

        //Output each rectangle
        outputRectangleInfo(rectangleList, verbose);
    }

    /**
     * <p>Parses the CommandLine input sent in and checks for filename, id for the first rectangle, and the id for the second rectangle</p>
     * <p>Each argument is required. If the filename is missing '.xml', it will add it</p>
     * <p>It will create the file from the filename and send it to {@link RectangleUtils} to retrieve the Rectangle objects</p>
     * <p>Will check for a rectangle for each id, and will check if they intersect</p>
     * <p>Will output each Rectangle using it's toString method and tell the user if they intersect</p>
     * @param cmd Commandline contains arguments for the file to be read in and an id for the two rectangles that are to be checked
     */
    public void intersect(CommandLine cmd) {
        // Check for all three arguments
        if (!cmd.getArgList().isEmpty() && cmd.getArgList().size() == 3) {
            checkFilenameExtension(cmd.getArgList().get(0));
            Integer idOne;
            Integer idTwo;

            // Try to parse the two id arguments
            try {
                // Grab each id from the user
                idOne = Integer.parseInt(cmd.getArgList().get(1));
                idTwo = Integer.parseInt(cmd.getArgList().get(2));

                // Grab list of rectangles
                List<Rectangle> rectangleList = getRectangleListFromFile();

                // Filter for each Rectangle, check if they are null
                List<Rectangle> rect1 = rectangleList.stream().filter(r -> r.getId() == idOne).collect(Collectors.toList());
                List<Rectangle> rect2 = rectangleList.stream().filter(r -> r.getId() == idTwo).collect(Collectors.toList());

                if (rect1 != null && !rect1.isEmpty() && rect1.get(0) != null && rect1.size() == 1) {
                    if (rect2 != null && !rect2.isEmpty() && rect2.get(0) != null && rect2.size() == 1) {
                        List<Rectangle> tempRectangleList = new ArrayList<>();
                        List<Pair<Integer, Integer>> intersectValues = rect1.get(0).getIntersect(rect2.get(0));

                        tempRectangleList.add(rect1.get(0));
                        tempRectangleList.add(rect2.get(0));

                        outputRectangleInfo(tempRectangleList, false);
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
                    } else {
                        log.error("ID: " + idTwo + " was not found among the Rectangles in the XML file");
                    }
                } else {
                    log.error("ID: " + idOne + " was not found among the Rectangles in the XML file");
                }
            } catch (NumberFormatException e) {
                log.error("Error parsing one of the ids passed in, please make sure the id is a number", e);
            }
        } else {
            log.error("The number of arguments is not 3. Please send in <filename> <id> <id>");
        }
    }

    /**
     * <p>Parses the CommandLine input sent in and checks for filename, id for the first rectangle, and the id for the second rectangle</p>
     * <p>Each argument is required. If the filename is missing '.xml', it will add it</p>
     * <p>It will create the file from the filename and send it to {@link RectangleUtils} to retrieve the Rectangle objects</p>
     * <p>Will check for a rectangle for each id, and will check if the first rectangle contains the second</p>
     * <p>Will output each Rectangle using it's toString method and tell the user if the first rectangle contains the second</p>
     * @param cmd Commandline contains arguments for the file to be read in and an id for the two rectangles that are to be checked
     */
    public void contain(CommandLine cmd) {
        // Check for all three arguments
        if (!cmd.getArgList().isEmpty() && cmd.getArgList().size() == 3) {
            checkFilenameExtension(cmd.getArgList().get(0));
            Integer idOne;
            Integer idTwo;

            // Try to parse the two id arguments
            try {
                // Grab each id from the user
                idOne = Integer.parseInt(cmd.getArgList().get(1));
                idTwo = Integer.parseInt(cmd.getArgList().get(2));

                // Grab list of rectangles
                List<Rectangle> rectangleList = getRectangleListFromFile();

                // Filter for each Rectangle, check if they are null
                List<Rectangle> rect1 = rectangleList.stream().filter(r -> r.getId() == idOne).collect(Collectors.toList());
                List<Rectangle> rect2 = rectangleList.stream().filter(r -> r.getId() == idTwo).collect(Collectors.toList());

                if (rect1 != null && !rect1.isEmpty() && rect1.get(0) != null && rect1.size() == 1) {
                    if (rect2 != null && !rect2.isEmpty() && rect2.get(0) != null && rect2.size() == 1) {
                        List<Rectangle> tempRectangleList = new ArrayList<>();
                        Boolean contain = rect1.get(0).doesContain(rect2.get(0));

                        tempRectangleList.add(rect1.get(0));
                        tempRectangleList.add(rect2.get(0));

                        outputRectangleInfo(tempRectangleList, false);
                        log.info("--------------------");
                        log.info("DOES RECTANGLE #1 CONTAIN RECTANGLE #2: " + ((contain) ? "Yes" : "No"));
                    } else {
                        log.error("ID: " + idTwo + " was not found among the Rectangles in the XML file");
                    }
                } else {
                    log.error("ID: " + idOne + " was not found among the Rectangles in the XML file");
                }
            } catch (NumberFormatException e) {
                log.error("Error parsing one of the ids passed in, please make sure the id is a number", e);
            }
        } else {
            log.error("The number of arguments is not 3. Please send in <filename> <id> <id>");
        }
    }

    /**
     * <p>Parses the CommandLine input sent in and checks for filename, id for the first rectangle, and the id for the second rectangle</p>
     * <p>Each argument is required. If the filename is missing '.xml', it will add it</p>
     * <p>It will create the file from the filename and send it to {@link RectangleUtils} to retrieve the Rectangle objects</p>
     * <p>Will check for a rectangle for each id, and will check if they are adjacent</p>
     * <p>Will output each Rectangle using it's toString method and tell the user both if they are adjacent and what type</p>
     * @param cmd Commandline contains arguments for the file to be read in and an id for the two rectangles that are to be checked
     */
    public void adjacent(CommandLine cmd) {
        // Check for all three arguments
        if (!cmd.getArgList().isEmpty() && cmd.getArgList().size() == 3) {
            checkFilenameExtension(cmd.getArgList().get(0));
            Integer idOne;
            Integer idTwo;

            // Try to parse the two id arguments
            try {
                // Grab each id from the user
                idOne = Integer.parseInt(cmd.getArgList().get(1));
                idTwo = Integer.parseInt(cmd.getArgList().get(2));

                // Grab list of rectangles
                List<Rectangle> rectangleList = getRectangleListFromFile();

                // Filter for each Rectangle, check if they are null
                List<Rectangle> rect1 = rectangleList.stream().filter(r -> r.getId() == idOne).collect(Collectors.toList());
                List<Rectangle> rect2 = rectangleList.stream().filter(r -> r.getId() == idTwo).collect(Collectors.toList());

                if (rect1 != null && !rect1.isEmpty() && rect1.get(0) != null && rect1.size() == 1) {
                    if (rect2 != null && !rect2.isEmpty() && rect2.get(0) != null && rect2.size() == 1) {
                        List<Rectangle> tempRectangleList = new ArrayList<>();
                        Rectangle.Adjacency adjacency = rect1.get(0).isAdjacent(rect2.get(0));

                        tempRectangleList.add(rect1.get(0));
                        tempRectangleList.add(rect2.get(0));

                        outputRectangleInfo(tempRectangleList, false);
                        log.info("--------------------");
                        log.info("IS RECTANGLE #1 & RECTANGLE #2 ADJACENT:  " + ((adjacency == Rectangle.Adjacency.NONE) ? "No" : "Yes"));
                        log.info("ADJACENT TYPE: " + returnStringFromEnum(adjacency));
                    } else {
                        log.error("ID: " + idTwo + " was not found among the Rectangles in the XML file");
                    }
                } else {
                    log.error("ID: " + idOne + " was not found among the Rectangles in the XML file");
                }
            } catch (NumberFormatException e) {
                log.error("Error parsing one of the ids passed in, please make sure the id is a number", e);
            }
        } else {
            log.error("The number of arguments is not 3. Please send in <filename> <id> <id>");
        }
    }

    // Helper functions
    private List<Rectangle> getRectangleListFromFile() {
        // Read in file
        File xmlFile = new File(filename);
        // Grab list of rectangles
        List<Rectangle> rectangleList = rectangleUtils.parseXmlToListOfRectangles(xmlFile);

        return  rectangleList;
    }

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
