package com.mhsnodgrass.rectangleparser.util;

import com.mhsnodgrass.rectangleparser.model.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/** Utility class for Rectangles.
 * @author Matthew Snodgrass
 */
@Slf4j
@Component
public class RectangleUtils {
    private final static String ID = "id";
    private final static String HEIGHT = "height";
    private final static String WIDTH = "width";
    private final static String X = "x";
    private final static String Y = "y";

    /**
     * <p>Parses the XML file input into Rectangle Entities by looping through each `rectangle` element found in XML file</p>
     * <p>Please see the {@link Rectangle} class for context</p>
     * <p>Grabs each element (id, height, width, etc) and assigns it to a variable, then creates the Rectangle using the values</p>
     * <p>If bad data is found (not an Integer, missing data, etc), it will skip the Rectangle</p>
     * <p>If a duplicate Id is found, it will skip the Rectangle</p>
     * @param xmlFile XML file to be parsed into Rectangle objects
     * @return A List of created Rectangles from the XML file
     */
    public List<Rectangle> parseXmlToListOfRectangles(File xmlFile) {
        // Create a list of Rectangles
        List<Rectangle> rectangleList = new ArrayList<>();
        // Create a set of ids to avoid duplicates
        HashSet<Integer> rectangleIds = new HashSet<>();

        try {
            // Create XML document
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            // Grab all rectangle nodes
            NodeList rectangles = document.getElementsByTagName("rectangle");

            // Loop through each node and create a rectangle, add it to the list
            for (int i = 0; i < rectangles.getLength(); i++) {
                // Grab each element
                Element element = (Element) rectangles.item(i);
                Integer id = getIntegerFromElement(element, ID);
                Integer height = getIntegerFromElement(element, HEIGHT);
                Integer width = getIntegerFromElement(element, WIDTH);
                Integer x = getIntegerFromElement(element, X);
                Integer y = getIntegerFromElement(element, Y);

                // Check if there was issues, skip
                if (id == null || height == null || width == null || x == null || y == null) {
                    log.warn("Rectangle at index " + i + " was not processed. Please check error to see what failed.");
                    continue;
                }

                // Check if id is in the HashSet
                if (rectangleIds.contains(id)) {
                    log.warn("Rectangle at index " + i + " was not processed. The Id of the Rectangle has already been used.");
                    continue;
                }
                rectangleIds.add(id);

                // Add to the rectangle list
                Rectangle rectangle = new Rectangle(id, height, width, x, y);
                rectangleList.add(rectangle);
            }
        } catch (ParserConfigurationException e) {
            log.error("Error creating XML document to parse", e);
        } catch (IOException e) {
            log.error("Error retrieving file: " + xmlFile.getName() + ".", e);
        } catch (SAXException e) {
            log.error("Error parsing file: " + xmlFile.getName() + ".", e);
        }

        return rectangleList;
    }

    private Integer getIntegerFromElement(Element element, String elementName) {
        String stringResult = "";
        Integer intResult;

        // Check each step of the element to avoid null exceptions
        if (element != null) {
            if (element.getElementsByTagName(elementName) != null) {
                if (element.getElementsByTagName(elementName).item(0) != null) {
                    if (element.getElementsByTagName(elementName).item(0).getTextContent() != null && !element.getElementsByTagName(elementName).item(0).getTextContent().isEmpty()) {
                        stringResult = element.getElementsByTagName(elementName).item(0).getTextContent();
                    } else {
                        log.error("Error getting the value of the " + elementName + " in the rectangle node, please check the XML file");
                    }
                } else {
                    log.error("Error accessing the " + elementName + " in the rectangle node, please check the XML file");
                }
            } else {
                log.error("Error finding the " + elementName + " in the rectangle node, please check the XML file");
            }
        } else {
            log.error("Error processing the entire rectangle node, please check the XML file");
        }

        // Null is handled by the main parse, checking if string was never grabbed
        if (stringResult.isEmpty()) {
            intResult = null;
        } else {
            try {
                intResult = Integer.parseInt(stringResult);
            } catch (NumberFormatException e) {
                log.error("Parsing the integer for " + elementName + " failed.", e);
                intResult = null;
            }
        }

        return intResult;
    }
}
