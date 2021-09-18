package com.mhsnodgrass.rectangleparser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandRunner implements CommandLineRunner {
    @Autowired
    private XmlParser xmlParser = new XmlParser();

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
                xmlParser.parse(line);
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
