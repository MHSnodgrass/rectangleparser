package com.mhsnodgrass.rectangleparser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
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
    private RectangleParser rectangleParser;

    /**
     * <p>Takes in the arguments from the command line and checks for options (-p, -h, etc)</p>
     * <p>-p parses the XML file provided as an argument (or uses a default name from application.properties).</p>
     * <p>-i parses the XML file and two ids from the user. Will compare each Rectangle with those ids to see if they intersect. Each argument is needed.</p>
     * <p>-p and -i Calls {@link RectangleParser} to process the file</p>
     * <p>-h Prints the help output</p>
     * <p>No arguments prints the help output</p>
     * @param args Arguments after an option declaration on the command line, currently looking for the filename of the XML file to be processed
     */
    public void run(String... args) {
        // Create options object to insert objects
        Options options = new Options();

        // Create the parsing option
        Option parse = Option.builder("p")
                .longOpt("Parse")
                .desc("Parses XML file into Rectangle entities and prints them to the output. You can define the filename after the option, or it will use the default from application.properties.")
                .argName("PARSE")
                .build();
        Option intersect = Option.builder("i")
                .longOpt("Intersect")
                .desc("Parses XML file into Rectangle entities, and takes two ids. It will compare each Rectangle to see if they intersect. Each argument is required.")
                .argName("INTERSECT")
                .build();
        Option help = Option.builder("h")
                .longOpt("Help")
                .desc("Prints this help message")
                .argName("HELP")
                .build();
        options.addOption(parse);
        options.addOption(intersect);
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
                rectangleParser.parse(line);
            // -i
            } else if (line.hasOption("i")){
                rectangleParser.intersect(line);
            // -h
            } else if (line.hasOption("h")) {
                formatter.printHelp("java -jar rectangleparser-0.0.1-SNAPSHOT.jar [-p <filename>] [-i <filename> <id> <id>]", options);
            // Default help message
            } else {
                formatter.printHelp("java -jar rectangleparser-0.0.1-SNAPSHOT.jar [-p <filename>] [-i <filename> <id> <id>]", options);
            }
        } catch (ParseException e) {
            log.error("Error parsing arguments/options", e);
        }
    }
}
