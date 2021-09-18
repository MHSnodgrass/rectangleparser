# Rectangle Parser

## Info
- Language: Java
- Frameworks: Spring Boot
- Added dependencies:
    - Commons CLI for command line
    - Lombok for easy logging

Currently, the program can:
- Parse an XML file to get rectangle data (height, width, x, y)
- Create rectangle objects based off of that data
- Print out the coordinates for each rectangle via output

## To Setup
- Have `Maven` installed
- Clone https://gitlab.com/MHSnodgrass/rectangleparser.git
- Build with `mvn install`

## How To Run
- Make sure to have an XML file with rectangle data in the same folder as the jar to run. Please see below for an example file.
- Run `java -jar rectangleparser-0.0.1-SNAPSHOT.jar -p <rectangles.xml>` to process the XML file, create rectangle entities, and print their coordinates to output.
###### Running with no arguments or using the -h option will give you a help screen
###### If you do not use an argument for -p, it will use the default from application.properties

## Example XML
```xml
<?xml version="1.0" encoding="UTF-8"?>
<rectangles>
	<rectangle>
		<height>5</height>
		<width>10</width>
		<x>0</x>
		<y>0</y>
	</rectangle>
	<rectangle>
		<height>10</height>
		<width>20</width>
		<x>10</x>
		<y>10</y>
	</rectangle>
</rectangles>
```

