package com.mhsnodgrass.rectangleparser;

import com.mhsnodgrass.rectangleparser.model.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;

@Slf4j
@Component
public class XmlParser {
    private final static String HEIGHT = "height";
    private final static String WIDTH = "width";
    private final static String X = "x";
    private final static String Y = "y";

    @Value("${rectangleparser.default.filename}")
    private String filename;

    public void parse(CommandLine cmd) {
        // Check if user provided name of XML file, if not, process default
        if (!cmd.getArgList().isEmpty() && cmd.getArgList().size() == 1) {
            filename = cmd.getArgList().get(0);
            filename = filename.endsWith(".xml") ? filename : filename + ".xml";
        } else {
            log.info("Filename was not provided, using default: " + filename);
        }

        // Create a list of Rectangles
        List<Rectangle> rectangleList = new ArrayList<>();

        // Read in file
        File xmlFile = new File(filename);

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
                Integer height = getIntegerFromElement(element, HEIGHT);
                Integer width = getIntegerFromElement(element, WIDTH);
                Integer x = getIntegerFromElement(element, X);
                Integer y = getIntegerFromElement(element, Y);

                // Check if there was issues, skip
                if (height == null || width == null || x == null || y == null) {
                    log.warn("Rectangle at index " + i + " was not processed. Please check error to see what failed.");
                    continue;
                }

                // Add to the rectangle list
                Rectangle rectangle = new Rectangle(height, width, x, y);
                rectangleList.add(rectangle);
            }
        } catch (ParserConfigurationException e) {
            log.error("Error creating XML document to parse", e);
        } catch (IOException e) {
            log.error("Error retrieving file: " + filename + ".", e);
        } catch (SAXException e) {
            log.error("Error parsing file: " + filename + ".", e);
        }

        //Output each rectangle
        for (int i = 0; i < rectangleList.size(); i++) {
            List<List<Integer>> coordinates = rectangleList.get(i).getCoordinates();
            log.info("--------------------");
            log.info("RECTANGLE #" + (i + 1));
            log.info("--------------------");
            log.info("TOP-LEFT: " + getCoordinatesFromList(coordinates, 0));
            log.info("TOP-RIGHT: " + getCoordinatesFromList(coordinates, 1));
            log.info("BOT-LEFT: " + getCoordinatesFromList(coordinates, 2));
            log.info("BOT-RIGHT: " + getCoordinatesFromList(coordinates, 3));
        }
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

    // Helper function to grab the coordinates and return them as a string
    private String getCoordinatesFromList(List<List<Integer>> coordinates, int index) {
        String x = coordinates.get(index).get(0).toString();
        String y = coordinates.get(index).get(1).toString();
        return x + ", " + y;
    }
}
