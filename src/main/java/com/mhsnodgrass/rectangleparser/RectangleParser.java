package com.mhsnodgrass.rectangleparser;

import com.mhsnodgrass.rectangleparser.model.Rectangle;
import com.mhsnodgrass.rectangleparser.util.RectangleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
public class RectangleParser {
    @Autowired
    private RectangleUtils rectangleUtils;

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

        // Read in file
        File xmlFile = new File(filename);

        // Grab list of rectangles
        List<Rectangle> rectangleList = rectangleUtils.parseXmlToListOfRectangles(xmlFile);

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

    // Helper function to grab the coordinates and return them as a string
    private String getCoordinatesFromList(List<List<Integer>> coordinates, int index) {
        String x = coordinates.get(index).get(0).toString();
        String y = coordinates.get(index).get(1).toString();
        return x + ", " + y;
    }
}
