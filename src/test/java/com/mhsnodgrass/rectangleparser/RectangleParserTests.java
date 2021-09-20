package com.mhsnodgrass.rectangleparser;

import com.mhsnodgrass.rectangleparser.model.Rectangle;
import com.mhsnodgrass.rectangleparser.util.RectangleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class RectangleParserTests {
    @Value("${rectangleparser.default.filename}")
    private String filename;

    @Autowired
    private RectangleUtils rectangleUtils;

    @Autowired
    private RectangleParser rectangleParser;

    private List<Rectangle> testRectangles;

    @BeforeAll
    public void loadRectanglesFromFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File xmlFile = new File(classLoader.getResource(filename).getFile());
        testRectangles = rectangleUtils.parseXmlToListOfRectangles(xmlFile);
    }

    @Test
    public void testIntersect() {
        Rectangle rect1 = testRectangles.get(2);
        Rectangle rect2 = testRectangles.get(3);

        assertThat(rect1).isNotNull();
        assertThat(rect2).isNotNull();

        List<Rectangle> tempList = new ArrayList<>();
        tempList.add(rect1);
        tempList.add(rect2);

        List<Pair<Integer, Integer>> result = rectangleParser.intersect(tempList);
        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getKey()).isEqualTo(5);
        assertThat(result.get(0).getValue()).isEqualTo(0);

        assertThat(result.get(1).getKey()).isEqualTo(5);
        assertThat(result.get(1).getValue()).isEqualTo(-5);
    }

    @Test
    public void testIntersectEmpty() {
        Rectangle rect1 = testRectangles.get(4);
        Rectangle rect2 = testRectangles.get(5);

        assertThat(rect1).isNotNull();
        assertThat(rect2).isNotNull();

        List<Rectangle> tempList = new ArrayList<>();
        tempList.add(rect1);
        tempList.add(rect2);

        List<Pair<Integer, Integer>> result = rectangleParser.intersect(tempList);
        assertThat(result).isEmpty();
    }

    @Test
    public void testContain() {
        Rectangle rect1 = testRectangles.get(8);
        Rectangle rect2 = testRectangles.get(9);

        assertThat(rect1).isNotNull();
        assertThat(rect2).isNotNull();

        List<Rectangle> tempList = new ArrayList<>();
        tempList.add(rect1);
        tempList.add(rect2);

        Boolean result = rectangleParser.contain(tempList);
        assertThat(result).isTrue();
    }

    @Test
    public void testContainFalse() {
        Rectangle rect1 = testRectangles.get(6);
        Rectangle rect2 = testRectangles.get(7);

        assertThat(rect1).isNotNull();
        assertThat(rect2).isNotNull();

        List<Rectangle> tempList = new ArrayList<>();
        tempList.add(rect1);
        tempList.add(rect2);

        Boolean result = rectangleParser.contain(tempList);
        assertThat(result).isFalse();
    }
}
