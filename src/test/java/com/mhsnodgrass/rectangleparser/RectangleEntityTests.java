package com.mhsnodgrass.rectangleparser;

import com.mhsnodgrass.rectangleparser.model.Rectangle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RectangleEntityTests {
    private Rectangle testRectangle = new Rectangle(10, 10, 0, 0);

    @Test
    public void testRectangleHeight() {
        assertThat(testRectangle).isNotNull();
        assertThat(testRectangle.getHeight()).isNotNull().isEqualTo(10);
    }

    @Test
    public void testRectangleWidth() {
        assertThat(testRectangle).isNotNull();
        assertThat(testRectangle.getWidth()).isNotNull().isEqualTo(10);
    }

    @Test
    public void testRectangleX() {
        assertThat(testRectangle).isNotNull();
        assertThat(testRectangle.getX()).isNotNull().isEqualTo(0);
    }

    @Test
    public void testRectangleY() {
        assertThat(testRectangle).isNotNull();
        assertThat(testRectangle.getY()).isNotNull().isEqualTo(0);
    }

    @Test
    public void testRectangleCoordinates() {
        assertThat(testRectangle).isNotNull();
        assertThat(testRectangle.getCoordinates()).isNotNull();

        // TOP-LEFT
        assertThat(testRectangle.getCoordinates().get(0));
        List<Integer> testCoordinates = testRectangle.getCoordinates().get(0);
        assertThat(testCoordinates.get(0)).isNotNull().isEqualTo(0);
        assertThat(testCoordinates.get(1)).isNotNull().isEqualTo(0);

        // TOP-RIGHT
        assertThat(testRectangle.getCoordinates().get(1));
        testCoordinates = testRectangle.getCoordinates().get(1);
        assertThat(testCoordinates.get(0)).isNotNull().isEqualTo(10);
        assertThat(testCoordinates.get(1)).isNotNull().isEqualTo(0);

        // BOT-LEFT
        assertThat(testRectangle.getCoordinates().get(2));
        testCoordinates = testRectangle.getCoordinates().get(2);
        assertThat(testCoordinates.get(0)).isNotNull().isEqualTo(0);
        assertThat(testCoordinates.get(1)).isNotNull().isEqualTo(-10);

        // BOT-RIGHT
        assertThat(testRectangle.getCoordinates().get(3));
        testCoordinates = testRectangle.getCoordinates().get(3);
        assertThat(testCoordinates.get(0)).isNotNull().isEqualTo(10);
        assertThat(testCoordinates.get(1)).isNotNull().isEqualTo(-10);
    }
}
