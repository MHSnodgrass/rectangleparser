package com.mhsnodgrass.rectangleparser;

import com.mhsnodgrass.rectangleparser.model.Rectangle;
import com.mhsnodgrass.rectangleparser.util.RectangleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** RectangleParser handles the {@link Rectangle} methods used by the {@link OutputHandler}
 * @author Matthew Snodgrass
 */
@Slf4j
@Component
public class RectangleParser {
    @Autowired
    private RectangleUtils rectangleUtils;

    /**
     * Takes in a List of two Rectangles and checks if they intersect
     * @param rect A list of two Rectangles
     * @return A list of pairs of coordinates that intersect between the two Rectangles
     */
    public List<Pair<Integer, Integer>> intersect(List<Rectangle> rect) {
       List<Pair<Integer, Integer>> results = null;

        if (rect != null) {
            results = rect.get(0).getIntersect(rect.get(1));
        }

        if (results.isEmpty()) {
            results = null;
        }

        return results;
    }

    /**
     * Takes in a List of two Rectangles and check if Rectangle #1 contains Rectangle #2
     * @param rect A list of two Rectangles
     * @return A Boolean representing if Rectangle #1 contains Rectangle #2
     */
    public Boolean contain(List<Rectangle> rect) {
        Boolean results = false;

        if (rect != null) {
            results = rect.get(0).doesContain(rect.get(1));
        }

        return results;
    }

    /**
     * Takes in a List of two Rectangles and checks if they are adjacent, and what type of adjacency is present
     * @param rect A List of two Rectangles
     * @return A enum value representing what type of adjacency is present (Proper, Sub-Line, Partial, or None)
     */
    public Rectangle.Adjacency adjacent(List<Rectangle> rect) {
        Rectangle.Adjacency results = Rectangle.Adjacency.NONE;

        if (rect != null) {
            results = rect.get(0).isAdjacent(rect.get(1));
        }

        return results;
    }

    /**
     * Takes in a filename, creates an XML file, and generates Rectangle Objects from that file using {@link RectangleUtils}
     * @param filename The filename for the file to be processed
     * @return A List of Rectangles from the XML file
     */
    public List<Rectangle> getRectangleListFromFile(String filename) {
        // Read in file
        File xmlFile = new File(filename);
        // Grab list of rectangles
        List<Rectangle> rectangleList = rectangleUtils.parseXmlToListOfRectangles(xmlFile);

        return  rectangleList;
    }

    /**
     * Takes in a List of Rectangles and filters it by two ids given by the user
     * @param rect A List of Rectangles
     * @param idOne First ID from the user
     * @param idTwo Second ID from the user
     * @return A new List containing two Rectangles
     */
    public List<Rectangle> filterRectanglesListByIds(List<Rectangle> rect, Integer idOne, Integer idTwo) {
        List<Rectangle> tempList = new ArrayList<>();

        // Grab each Rectangle by their Id, add them to list
        List<Rectangle> rect1 = rect.stream().filter(r -> r.getId() == idOne).collect(Collectors.toList());
        List<Rectangle> rect2 = rect.stream().filter(r -> r.getId() == idTwo).collect(Collectors.toList());

        // Confirm Rectangles were found
        if (rect1 != null && !rect1.isEmpty() && rect1.get(0) != null && rect1.size() == 1) {
            if (rect2 != null && !rect2.isEmpty() && rect2.get(0) != null && rect2.size() == 1) {
                // Grab each Rectangle and add them to a single list
                tempList.add(rect1.get(0));
                tempList.add(rect2.get(0));
            } else {
                tempList = null;
                log.error("ID: " + idTwo + " was not found among the Rectangles in the XML file");
            }
        } else {
            tempList = null;
            log.error("ID: " + idOne + " was not found among the Rectangles in the XML file");
        }

        return tempList;
    }
}
