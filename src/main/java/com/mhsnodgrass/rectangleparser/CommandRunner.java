package com.mhsnodgrass.rectangleparser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** CommandRunner takes in the arguments from the command line to run the rest of the program, implements CommandLineRunner to always run
 * @author Matthew Snodgrass
 */
@Component
@Slf4j
public class CommandRunner implements CommandLineRunner {
    @Autowired
    private OutputHandler outputHandler;

    /**
     * <p>Takes in the arguments from the command line and checks for options (-p, -h, etc).</p>
     * <p>-p parses the XML file provided as an argument (or uses a default name from application.properties).</p>
     * <p>-i parses the XML file and two ids from the user. Will compare each Rectangle with those ids to see if they intersect. Each argument is needed.</p>
     * <p>-c parses the XML file and two ids from the user. Will check if Rectangle #1 contains Rectangle #2. Each argument is needed.</p>
     * <p>-j parses the XML file and two ids from the user. Will check if the Rectangles intersect, and print every intersection coordinate. Each argument is needed.</p>
     * <p>'v' can be added to every option (other than -h) to print out every coordinate of the Rectangles that are processed.</p>
     * <p>{@link OutputHandler} is used to handle each option.</p>
     * <p>-h Prints the help output</p>
     * <p>No arguments prints the help output</p>
     * @param args Arguments after an option declaration on the command line, currently looking for the filename for -p, filename, id1, and id2 for all others
     */
    public void run(String... args) {
        // Create options object to insert objects
        Options options = new Options();

        // Create the parsing option
        Option parse = Option.builder("p")
                .longOpt("Parse")
                .desc("Parses the XML file into Rectangle entities and prints them to the output. You can define the filename after the option, or it will use the default from application.properties.")
                .argName("PARSE")
                .build();
        Option parseVerbose = Option.builder("pv")
                .longOpt("ParseVerbose")
                .desc("Same as -p, but it prints out every coordinate of the rectangles being processed.")
                .argName("PARSEVERBOSE")
                .build();
        Option intersect = Option.builder("i")
                .longOpt("Intersect")
                .desc("Parses the XML file into Rectangle entities, and takes two ids. It will compare each Rectangle to see if they intersect. Each argument is required.")
                .argName("INTERSECT")
                .build();
        Option intersectVerbose = Option.builder("iv")
                .longOpt("IntersectVerbose")
                .desc("Same as -i, but it prints out every coordinate of the rectangles being processed.")
                .argName("INTERSECTVERBOSE")
                .build();
        Option contain = Option.builder("c")
                .longOpt("Contain")
                .desc("Parses the XML file into Rectangle entities, and takes two ids. It will check if the first Rectangle contains the second. Each argument is required.")
                .argName("CONTAIN")
                .build();
        Option containVerbose = Option.builder("cv")
                .longOpt("ContainVerbose")
                .desc("Same as -c, but it prints out every coordinate of the rectangles being processed.")
                .argName("CONTAINVERBOSE")
                .build();
        Option adjacent = Option.builder("j")
                .longOpt("Adjacent")
                .desc("Parses the XML file into Rectangle entties, and takes two ids. It will check if the two Rectangles are adjacent and return what type if they are. Each argument is required.")
                .argName("ADJACENT")
                .build();
        Option adjacentVerbose = Option.builder("jv")
                .longOpt("AdjacentVerbose")
                .desc("Same as -j, but it prints out every coordinate of the rectangles being processed.")
                .argName("ADJACENTVERBOSE")
                .build();
        Option help = Option.builder("h")
                .longOpt("Help")
                .desc("Prints this help message")
                .argName("HELP")
                .build();

        options.addOption(parse);
        options.addOption(parseVerbose);
        options.addOption(intersect);
        options.addOption(intersectVerbose);
        options.addOption(contain);
        options.addOption(containVerbose);
        options.addOption(adjacent);
        options.addOption(adjacentVerbose);
        options.addOption(help);

        // Create parser
        CommandLineParser parser = new DefaultParser();

        // Create help message
        HelpFormatter formatter = new HelpFormatter();

        // Parse the option
        try {
            // Parse the options/args coming from command line arguments, compare to declared options
            CommandLine line = parser.parse(options, args);

            // -p
            if (line.hasOption("p")) {
                outputHandler.printRectangleList(line, false);
            // -pv
            } else if (line.hasOption("pv")) {
                outputHandler.printRectangleList(line, true);
            // -i
            } else if (line.hasOption("i")) {
                outputHandler.printIntersectionCoordinates(line, false);
            // -iv
            } else if (line.hasOption("iv")) {
                outputHandler.printIntersectionCoordinates(line, true);
            // c
            } else if (line.hasOption("c")) {
                outputHandler.printContainment(line, false);
            // -cv
            } else if (line.hasOption("cv")) {
                outputHandler.printContainment(line, true);
            // j
            } else if (line.hasOption("j")) {
                outputHandler.printAdjacency(line, false);
            // -jv
            } else if (line.hasOption("jv")) {
                outputHandler.printAdjacency(line, true);
            // -h
            } else if (line.hasOption("h")) {
                formatter.printHelp("java -jar rectangleparser-0.0.1-SNAPSHOT.jar [-p | -pv <filename>] [[-i, -iv, -c, -cv, -j, -jv] <filename> <id> <id>]", options);
            // Default help message
            } else {
                formatter.printHelp("java -jar rectangleparser-0.0.1-SNAPSHOT.jar [-p | -pv <filename>] [[-i, -iv, -c, -cv, -j, -jv] <filename> <id> <id>]", options);
            }
        } catch (ParseException e) {
            log.error("Error parsing arguments/options", e);
        }
    }
}
