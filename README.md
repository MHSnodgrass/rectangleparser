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

## To Setup
- Have [Maven](https://www.baeldung.com/install-maven-on-windows-linux-mac) installed
- Clone https://gitlab.com/MHSnodgrass/rectangleparser.git
- CD into `rectangleparser`
- Build with `mvn install`
- Javadocs can be created using `mvn javadoc:javadoc`

## How To Run
- Make sure to have an XML file with rectangle data in the same folder as the jar to run. Please see below for an example file.
- CD into `target`
###### Running with no arguments or using the -h option will give you a help screen
###### If you do not use an argument for -p, it will use the default from application.properties
- Run `java -jar rectangleparser-0.0.1-SNAPSHOT.jar -p <rectangles.xml>` to process the XML file, create rectangle entities, and print their coordinates to output.
###### All arguments are required for -i
- Run `java -jar rectangleparser-0.0.1-SNAPSHOT.jar -i <rectangles.xml> <id> <id>` to process the XML file, create rectangle entites, find each rectangle based on the two ids sent in, and check if those rectangles intersect. It will print an output to the user with the answer.

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

