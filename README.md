# Rectangle Parser

## Info
- Language: Java (JDK-11)
- Frameworks: Spring Boot
- Build Tools: Maven
- Added dependencies:
    - Commons CLI for command line
    - Lombok for easy logging
    - Maven-javadoc-plugin for easy javadoc creation

Currently, the program can:
- Parse an XML file to get rectangle data (id, height, width, x, y)
- Create rectangle objects based off of that data
- Print out the data for the rectangle, including coordinates for each point via output
- Check if two rectangles, based on ids sent in by the user, intersect and return the answer via output
- Check if, based on ids sent in by the user, a rectangle contains another rectangle
- Checks if two rectangles, based on ids sent in by the user, are adjacent. Also tells the user what type of adjacency is present (Proper, Sub-Line, Partial, None)

## To Setup
- Have [Maven](https://www.baeldung.com/install-maven-on-windows-linux-mac) installed
- Clone https://gitlab.com/MHSnodgrass/rectangleparser.git
- CD into `rectangleparser`
- Build with `mvn install`
- CD into `target`
- Run with one of the commands below  
- Javadocs can be created using `mvn javadoc:javadoc`
- Output file is set by `application.properties` and is set as `rectangleOutput.log` by default

###### Make sure to have an XML file to use. There is a default one included, `rectangles.xml`, just copy it to the `target` folder before running
###### The Rectangles in the example XML file have attributes to help you test the program. The attribute is `tryWith` and helps you try out the different options in the program
###### An example XML file is below if you want to see the file structure needed

## How To Run
###### Running with no arguments or using the -h option will give you a help screen
###### You can add `v` to any option (ie: `-iv`, `-pv`, etc) for verbose mode. This will give you every coordinate of the rectangle in the output

###### If you do not use an argument for -p, it will use the default from application.properties
- Run `java -jar rectangleparser-0.0.1-SNAPSHOT.jar -p <rectangles.xml>` to process the XML file, create rectangle entities, and print their coordinates to output.
###### All arguments are required for -i
- Run `java -jar rectangleparser-0.0.1-SNAPSHOT.jar -i <rectangles.xml> <id> <id>` to process the XML file, create rectangle entities, find each rectangle based on the two ids sent in, and check if those rectangles intersect. It will print an output to the user with the answer.
###### All arguments are required for -c
- Run `java -jar rectangleparser-0.0.1-SNAPSHOT.jar -i <rectangles.xml> <id> <id>` to process the XML file, create rectangle entities, find each rectangle based on the two ids sent in, and check if the first rectangles contains the second. It will print an output to the user with the answer.
###### All arguments are required for -j
- Run `java -jar rectangleparser-0.0.1-SNAPSHOT.jar -j <rectangles.xml> <id> <id>` to process the XML file, create rectangle entities, find each rectangle based on the two ids sent in, and check if the rectangles are adjacent, and what type of adjacency is present. It will print an output to the user with the answer.
###### All arguments are required for -a
- Run `java -jar rectangleparser-0.0.1-SNAPSHOT.jar -a <rectangles.xml> <id> <id>` to process the XML file, create rectangle entities, find each rectangle based on the two ids sent in, and try all the previous methods together. It will print a combined output to the user with the answer.

## Example Call
`java -jar rectangleparser-0.0.1-SNAPSHOT.jar -i rectangles.xml 3 4` using the default document provided:
```bash
C:\Users\matth\Projects\rectangleparser\target>java -jar rectangleparser-0.0.1-SNAPSHOT.jar -i rectangles.xml 3 4
,--.   ,--. ,--.  ,--.  ,---.                       ,--.
|   `.'   | |  '--'  | '   .-'  ,--,--,   ,---.   ,-|  |  ,---.  ,--.--.  ,--,--.  ,---.   ,---.
|  |'.'|  | |  .--.  | `.  `-.  |      \ | .-. | ' .-. | | .-. | |  .--' ' ,-.  | (  .-'  (  .-'
|  |   |  | |  |  |  | .-'    | |  ||  | ' '-' ' \ `-' | ' '-' ' |  |    \ '-'  | .-'  `) .-'  `)
`--'   `--' `--'  `--' `-----'  `--''--'  `---'   `---'  .`-  /  `--'     `--`--' `----'  `----'
                                                         `---'
 ___              _                          _           ___
| _ \  ___   __  | |_   __ _   _ _    __ _  | |  ___    | _ \  __ _   _ _   ___  ___   _ _
|   / / -_) / _| |  _| / _` | | ' \  / _` | | | / -_)   |  _/ / _` | | '_| (_-< / -_) | '_|
|_|_\ \___| \__|  \__| \__,_| |_||_| \__, | |_| \___|   |_|   \__,_| |_|   /__/ \___| |_|
                                     |___/
2021-09-19 22:16:07.882  INFO 184836 --- [           main] c.m.r.RectangleparserApplication         : Starting RectangleparserApplication v0.0.1-SNAPSHOT using Java 11.0.11 on DESKTOP-O01K28T with PID 184836 (C:\Users\matth\Projects\rectangleparser\target\rectangleparser-0.0.1-SNAPSHOT.jar started by matth in C:\Users\matth\Projects\rectangleparser\target)
2021-09-19 22:16:07.884  INFO 184836 --- [           main] c.m.r.RectangleparserApplication         : No active profile set, falling back to default profiles: default
2021-09-19 22:16:08.469  INFO 184836 --- [           main] c.m.r.RectangleparserApplication         : Started RectangleparserApplication in 0.99 seconds (JVM running for 1.368)
2021-09-19 22:16:08.506  INFO 184836 --- [           main] c.m.rectangleparser.OutputHandler        : --------------------
2021-09-19 22:16:08.508  INFO 184836 --- [           main] c.m.rectangleparser.OutputHandler        : RECTANGLE #1
2021-09-19 22:16:08.529  INFO 184836 --- [           main] c.m.rectangleparser.OutputHandler        : ID: 3, WIDTH: 10, HEIGHT: 5 | COORDINATES: TL: (0, 0) / TR: (10, 0) / BL: (0, -5) / BR: (10, -5)
2021-09-19 22:16:08.530  INFO 184836 --- [           main] c.m.rectangleparser.OutputHandler        : --------------------
2021-09-19 22:16:08.530  INFO 184836 --- [           main] c.m.rectangleparser.OutputHandler        : RECTANGLE #2
2021-09-19 22:16:08.530  INFO 184836 --- [           main] c.m.rectangleparser.OutputHandler        : ID: 4, WIDTH: 20, HEIGHT: 15 | COORDINATES: TL: (5, 5) / TR: (25, 5) / BL: (5, -10) / BR: (25, -10)
2021-09-19 22:16:08.530  INFO 184836 --- [           main] c.m.rectangleparser.OutputHandler        : --------------------
2021-09-19 22:16:08.532  INFO 184836 --- [           main] c.m.rectangleparser.OutputHandler        : DOES RECTANGLE #2 INTERSECT RECTANGLE #1: Yes
2021-09-19 22:16:08.541  INFO 184836 --- [           main] c.m.rectangleparser.OutputHandler        : INTERSECTING COORDINATES: (5, 0) | (5, -5)
```
## Example XML
```xml
<?xml version="1.0" encoding="UTF-8"?>
<rectangles>
	<rectangle>
		<id>1</id>
		<height>5</height>
		<width>10</width>
		<x>0</x>
		<y>0</y>
	</rectangle>
	<rectangle>
		<id>2</id>
		<height>10</height>
		<width>20</width>
		<x>10</x>
		<y>10</y>
	</rectangle>
</rectangles>
```

