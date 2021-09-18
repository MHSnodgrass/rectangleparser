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
     * <p>-p Calls {@link RectangleParser} to process the file</p>
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
                .desc("Parses XML file into rectangle entities. You can define the filename after the option, or it will use the default from application.properties")
                .argName("PARSE")
                .build();
        Option help = Option.builder("h")
                .longOpt("Help")
                .desc("Prints this help message")
                .argName("HELP")
                .build();
        options.addOption(parse);
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
            // -h
            } else if (line.hasOption("h")) {
                formatter.printHelp("java -jar rectangleparser-0.0.1-SNAPSHOT.jar -p [filename]", options);
            // Default help message
            } else {
                formatter.printHelp("java -jar rectangleparser-0.0.1-SNAPSHOT.jar -p [filename]", options);
            }
        } catch (ParseException e) {
            log.error("Error parsing arguments/options", e);
        }
    }
}
