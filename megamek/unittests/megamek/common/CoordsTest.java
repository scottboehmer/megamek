/*
 * Copyright (c) 2024 - The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MegaMek.
 *
 * MegaMek is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MegaMek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MegaMek. If not, see <http://www.gnu.org/licenses/>.
 */
package megamek.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CoordsTest {

    @Test
    void testTranslated() {
        assertEquals(new Coords(1, 1), new Coords(0, 0).translated(2));
        assertEquals(new Coords(2, 0), new Coords(1, 0).translated(2));
        assertEquals(new Coords(3, 2), new Coords(2, 1).translated(2));

        assertEquals(new Coords(0, 1), new Coords(0, 0).translated(3));
        assertEquals(new Coords(6, -3), new Coords(7, -2).translated(5));

        assertEquals(new Coords(3, 2), new Coords(0, 0).translated(2, 3));
        assertEquals(new Coords(10, 2), new Coords(10, 4).translated(0, 2));
        assertEquals(new Coords(10, 9), new Coords(10, 4).translated(3, 5));
        assertEquals(new Coords(7, 6), new Coords(10, 4).translated(4, 3));
    }

    @Test
    void testDistance() {
        assertEquals(new Coords(13, 6).distance(new Coords(15, 1)), 6);
        assertEquals(new Coords(12, 2).distance(new Coords(9, 2)), 3);
    }

    @Test
    void testAdjacent() {
        assertEquals(new Coords(5, -5).allAtDistance(0).size(), 1);

        final List<Coords> expectedAdjacent = new ArrayList<>();
        expectedAdjacent.add(new Coords(0, -1));
        expectedAdjacent.add(new Coords(1, 0));
        expectedAdjacent.add(new Coords(1, 1));
        expectedAdjacent.add(new Coords(0, 1));
        expectedAdjacent.add(new Coords(-1, 1));
        expectedAdjacent.add(new Coords(-1, 0));
        assertEquals(new Coords(0, 0).allAdjacent().size(), 6);
        new Coords(0, 0).allAdjacent().forEach(coords -> assertTrue(expectedAdjacent.contains(coords)));

        // for a radius 2 donut we expect to see 12 hexes.
        final List<Coords> expectedAtDistance2 = new ArrayList<>();
        expectedAtDistance2.add(new Coords(0, -2));
        expectedAtDistance2.add(new Coords(1, -1));
        expectedAtDistance2.add(new Coords(2, -1));
        expectedAtDistance2.add(new Coords(2, 0));
        expectedAtDistance2.add(new Coords(2, 1));
        expectedAtDistance2.add(new Coords(1, 2));
        expectedAtDistance2.add(new Coords(0, 2));
        expectedAtDistance2.add(new Coords(-1, 2));
        expectedAtDistance2.add(new Coords(-2, 1));
        expectedAtDistance2.add(new Coords(-2, 0));
        expectedAtDistance2.add(new Coords(-2, -1));
        expectedAtDistance2.add(new Coords(-1, -1));

        assertEquals(new Coords(0, 0).allAtDistance(2).size(), 12);
        new Coords(0, 0).allAtDistance(2).forEach(coords -> assertTrue(expectedAtDistance2.contains(coords)));
    }

    @Test
    void testAllAtDistance() {
        assertEquals(new Coords(10, 10).allAtDistanceOrLess(1).size(), 7);
        assertEquals(new Coords(10, 10).allLessThanDistance(1).size(), 1);
        assertEquals(new Coords(10, 10).allAtDistanceOrLess(0).size(), 1);
    }

    List<Coords> generateLevel2Neighbors(Coords centroid){
        return Arrays.asList(
            new Coords(centroid.getX(), centroid.getY()),
            // immediate neighbors
            new Coords(centroid.getX(), centroid.getY() - 1),
            new Coords(centroid.getX() + 1, centroid.getY() - 1),
            new Coords(centroid.getX() + 1, centroid.getY()),
            new Coords(centroid.getX(), centroid.getY() + 1),
            new Coords(centroid.getX() - 1, centroid.getY()),
            new Coords(centroid.getX() - 1, centroid.getY() - 1),
            // neighbors + 1
            new Coords(centroid.getX(), centroid.getY() - 2),
            new Coords(centroid.getX() + 1, centroid.getY() - 2),
            new Coords(centroid.getX() + 2, centroid.getY() - 1),
            new Coords(centroid.getX() + 2, centroid.getY()),
            new Coords(centroid.getX() + 2, centroid.getY() + 1),
            new Coords(centroid.getX() + 1, centroid.getY() + 1),
            new Coords(centroid.getX(), centroid.getY() + 2),
            new Coords(centroid.getX() - 1, centroid.getY() + 1),
            new Coords(centroid.getX() - 2, centroid.getY() + 1),
            new Coords(centroid.getX() - 2, centroid.getY()),
            new Coords(centroid.getX() - 2, centroid.getY() - 1),
            new Coords(centroid.getX() - 1, centroid.getY() - 2)
        );
    }

    @Test
    void testAllAtDistanceOrLessAlignedCorrectly() {
        Coords centroid = new Coords(7, 7);
        List<Coords> neighbors = centroid.allAtDistanceOrLess(2);
        List<Coords> expectedNeighbors = generateLevel2Neighbors(centroid);
        assertEquals(19, neighbors.size());
        assertEquals(19, expectedNeighbors.size());

        // All generated neighbors must be in expectedNeighbors
        boolean allMatch = true;
        List<Coords> mismatches = new ArrayList<>();
        for (Coords coords : neighbors) {
            if (expectedNeighbors.stream().anyMatch(coords::equals)) {
                continue;
            }
            allMatch = false;
            mismatches.add(coords);
        }
        String mismatchString = mismatches.stream().map(Coords::toString).collect(Collectors.joining(", "));
        assertTrue(allMatch, mismatchString);

        // All generated expectedNeighbors must be in neighbors
        allMatch = true;
        mismatches = new ArrayList<>();
        for (Coords coords : expectedNeighbors) {
            if (neighbors.stream().anyMatch(coords::equals)) {
                continue;
            }
            allMatch = false;
            mismatches.add(coords);
        }
        mismatchString = mismatches.stream().map(Coords::toString).collect(Collectors.joining(", "));
        assertTrue(allMatch, mismatchString);
    }

    @Test
    void testTranslation() {
        Coords center = new Coords(8, 9);
        assertEquals(new Coords(7, 10), center.translated(4, 1));
        assertEquals(new Coords(7, 9), center.translated(5, 1));
    }

    @Test
    void testHexRow() {
        Coords center = new Coords(3, 7);
        assertFalse(center.isOnHexRow(-1, new Coords(0, 0)));
        assertFalse(center.isOnHexRow(6, new Coords(0, 0)));
        assertFalse(center.isOnHexRow(2, center));
        assertFalse(center.isOnHexRow(5, center));
        assertFalse(center.isOnHexRow(2, null));

        Coords other = new Coords(3, 5);
        assertTrue(center.isOnHexRow(0, other));
        assertFalse(center.isOnHexRow(1, other));
        assertFalse(center.isOnHexRow(2, other));
        assertFalse(center.isOnHexRow(3, other));
        assertFalse(center.isOnHexRow(4, other));
        assertFalse(center.isOnHexRow(5, other));

        other = new Coords(3, -1);
        assertTrue(center.isOnHexRow(0, other));
        assertFalse(center.isOnHexRow(1, other));
        assertFalse(center.isOnHexRow(2, other));
        assertFalse(center.isOnHexRow(3, other));
        assertFalse(center.isOnHexRow(4, other));
        assertFalse(center.isOnHexRow(5, other));

        other = new Coords(4, -1);
        assertFalse(center.isOnHexRow(0, other));
        assertFalse(center.isOnHexRow(1, other));
        assertFalse(center.isOnHexRow(2, other));
        assertFalse(center.isOnHexRow(3, other));
        assertFalse(center.isOnHexRow(4, other));
        assertFalse(center.isOnHexRow(5, other));

        center = new Coords(8, 2);
        other = new Coords(8, 8);
        assertFalse(center.isOnHexRow(0, other));
        assertFalse(center.isOnHexRow(1, other));
        assertFalse(center.isOnHexRow(2, other));
        assertTrue(center.isOnHexRow(3, other));
        assertFalse(center.isOnHexRow(4, other));
        assertFalse(center.isOnHexRow(5, other));

        center = new Coords(4, 4);
        other = new Coords(8, 2);
        assertFalse(center.isOnHexRow(0, other));
        assertTrue(center.isOnHexRow(1, other));
        assertFalse(center.isOnHexRow(2, other));
        assertFalse(center.isOnHexRow(3, other));
        assertFalse(center.isOnHexRow(4, other));
        assertFalse(center.isOnHexRow(5, other));

        other = new Coords(8, 3);
        assertFalse(center.isOnHexRow(0, other));
        assertFalse(center.isOnHexRow(1, other));
        assertFalse(center.isOnHexRow(2, other));
        assertFalse(center.isOnHexRow(3, other));
        assertFalse(center.isOnHexRow(4, other));
        assertFalse(center.isOnHexRow(5, other));
    }

    @Test
    void testHex2HexInterveningDistance1() {
        Coords source = new Coords(5, 5);
        Coords target = new Coords(6, 5);

        List<Coords> intervening = Coords.intervening(source, target);
        assertFalse(intervening.isEmpty());
    }

    @Test
    void testHex2HexRadianDirections() {
        // Test identity - docs say equals 0?
        Coords source = new Coords(5, 5);
        Coords target1 = new Coords(5, 5);
        assertEquals(4.7, source.radian(target1), 0.1);
        assertEquals(5, source.direction(target1));

        // Test one away to north
        Coords target2 = new Coords(5, 4);
        assertEquals(0, source.radian(target2));
        assertEquals(0, source.direction(target2));

        // Test one away NE
        Coords target3 = new Coords(6, 4);
        assertEquals(1.047, source.radian(target3), 0.01);
        assertEquals(1, source.direction(target3));

        // Test one away SE
        Coords target4 = new Coords(6, 5);
        assertEquals(2.1, source.radian(target4), 0.1);
        assertEquals(2, source.direction(target4));

        // Test one away SE
        Coords target5 = new Coords(7, 5);
        assertEquals(Math.PI / 2, source.radian(target5), 0.1);
        assertEquals(2, source.direction(target5));

    }

    @Test
    void testHex2HexDistance() {
        // Dist from 0702 to 0601 should be 1, not 2.
        Coords source = new Coords(7, 2);
        Coords target1 = new Coords(6, 1);
        assertEquals(1, source.distance(target1));

        // Dist from 0601 to 0702 should be 1
        source = new Coords(6, 1);
        target1 = new Coords(7, 2);
        assertEquals(1, source.distance(target1));

        // Dist from 0602 to 0703 should be 1, not 2.
        source = new Coords(6, 2);
        target1 = new Coords(7, 3);
        assertEquals(1, source.distance(target1));

        source = new Coords(13, 5);
        target1 = new Coords(5, 6);
        assertEquals(8, source.distance(target1));

        source = new Coords(2, 16);
        target1 = new Coords(14, 10);
        assertEquals(12, source.distance(target1));
    }
}
