package com.mhsnodgrass.rectangleparser;

import com.mhsnodgrass.rectangleparser.model.Rectangle;
import com.mhsnodgrass.rectangleparser.util.RectangleUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class RectangleUtilsTests {
    @Value("${rectangleparser.default.filename}")
    private String filename;

    @Autowired
    RectangleUtils rectangleUtils;

    private List<Rectangle> testRectangles;

    @BeforeAll
    public void loadRectanglesFromFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File xmlFile = new File(classLoader.getResource(filename).getFile());
        testRectangles = rectangleUtils.parseXmlToListOfRectangles(xmlFile);
    }

    // Regular Rectangle Tests
    @Test
    public void testRectangleListSize() {
        // List length should be 8
        // This tests if items are skipped if they are missing data (Rectangle #3)
        // This also tests if rectangles with Ids already in use are skipped (Rectangle #10)
        assertThat(testRectangles).isNotNull();
        assertThat(testRectangles.size()).isEqualTo(8);
    }

    @Test
    public void testFirstRectangle() {
        Rectangle rect = testRectangles.get(0);
        testRectangleFields(rect, 1, 5, 10, 0, 0);
        testRectangleXCoordinates(rect, 0, 10, 0, 10);
        testRectangleYCoordinates(rect, 0, 0, -5, -5);
    }

    @Test
    public void testSecondRectangle() {
        Rectangle rect = testRectangles.get(1);
        testRectangleFields(rect, 2, 10, 20, 10, 10);
        testRectangleXCoordinates(rect, 10, 30, 10, 30);
        testRectangleYCoordinates(rect, 10, 10, 0, 0);
    }

    // Intersection Tests
    @Test
    public void testIntersectingRectangles() {
        Rectangle rect1 = testRectangles.get(2);
        Rectangle rect2 = testRectangles.get(3);

        assertThat(rect1).isNotNull();
        assertThat(rect2).isNotNull();

        assertThat(rect1.doesIntersect(rect2)).isTrue();
    }

    @Test
    public void testNonIntersectingRectangles() {
        Rectangle rect1 = testRectangles.get(4);
        Rectangle rect2 = testRectangles.get(5);

        assertThat(rect1).isNotNull();
        assertThat(rect2).isNotNull();

        assertThat(rect1.doesIntersect(rect2)).isFalse();
    }

    @Test
    public void testPerfectOverlap() {
        Rectangle rect1 = testRectangles.get(6);
        Rectangle rect2 = testRectangles.get(7);

        assertThat(rect1).isNotNull();
        assertThat(rect2).isNotNull();

        assertThat(rect1.doesIntersect(rect2)).isTrue();
    }

    // Helper Functions
    public void testRectangleFields(Rectangle rect, Integer id, Integer height, Integer width, Integer x, Integer y) {
        assertThat(rect).isNotNull();
        assertThat(rect.getId()).isNotNull().isEqualTo(id);
        assertThat(rect.getHeight()).isNotNull().isEqualTo(height);
        assertThat(rect.getWidth()).isNotNull().isEqualTo(width);
        assertThat(rect.getX()).isNotNull().isEqualTo(x);
        assertThat(rect.getY()).isNotNull().isEqualTo(y);
    }

    public void testRectangleXCoordinates(Rectangle rect, Integer topLeft, Integer topRight, Integer botLeft, Integer botRight) {
        assertThat(rect).isNotNull();
        List<List<Integer>> coordinates = rect.getCoordinates();

        assertThat(coordinates).isNotNull();
        assertThat(coordinates.size()).isEqualTo(4);

        assertThat(coordinates.get(0).get(0)).isNotNull().isEqualTo(topLeft);
        assertThat(coordinates.get(1).get(0)).isNotNull().isEqualTo(topRight);
        assertThat(coordinates.get(2).get(0)).isNotNull().isEqualTo(botLeft);
        assertThat(coordinates.get(3).get(0)).isNotNull().isEqualTo(botRight);
    }

    public void testRectangleYCoordinates(Rectangle rect, Integer topLeft, Integer topRight, Integer botLeft, Integer botRight) {
        assertThat(rect).isNotNull();
        List<List<Integer>> coordinates = rect.getCoordinates();

        assertThat(coordinates).isNotNull();
        assertThat(coordinates.size()).isEqualTo(4);

        assertThat(coordinates.get(0).get(1)).isNotNull().isEqualTo(topLeft);
        assertThat(coordinates.get(1).get(1)).isNotNull().isEqualTo(topRight);
        assertThat(coordinates.get(2).get(1)).isNotNull().isEqualTo(botLeft);
        assertThat(coordinates.get(3).get(1)).isNotNull().isEqualTo(botRight);
    }
}
