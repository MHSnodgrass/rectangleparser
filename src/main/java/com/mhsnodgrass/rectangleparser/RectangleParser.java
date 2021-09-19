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
     * <p>Will output each Rectangle's top left, top right, bottom left, and bottom right x, y coordinates</p>
     * @param cmd Commandline contains any arguments from the user for changing what file should be read in
     */
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
            log.info(rectangleList.get(i).toString());
        }
    }
}
