package org.simulation.dto;


import org.simulation.entitys.Entity;

import java.util.function.Predicate;

public class NeighbourFinder {
    private static final int[][] DIRECTIONS = {
            {0, 1},  // вниз
            {1, 0},  // вправо
            {0, -1}, // вверх
            {-1, 0}  // влево
    };

    public static Coordinates findNeighbour(GameMap map, Coordinates currentPosition, Predicate<Entity> condition) {
        for (int[] direction : DIRECTIONS) {
            Coordinates neighbour = new Coordinates(
                    currentPosition.getX() + direction[0],
                    currentPosition.getY() + direction[1]
            );
            if (map.isWithinBounds(neighbour)) {
                if (condition.test(map.getEntity(neighbour))) {
                    return neighbour;
                }
            }
        }
        return null;
    }
}