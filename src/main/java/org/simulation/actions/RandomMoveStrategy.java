package org.simulation.actions;

import org.simulation.dto.Coordinates;
import org.simulation.dto.GameMap;
import org.simulation.entitys.creatures.Creature;

import java.util.concurrent.ThreadLocalRandom;

public class RandomMoveStrategy implements MoveStrategy {
    @Override
    public void move(Creature creature, GameMap map) {
        Coordinates currentPosition = creature.getPosition();
        if (currentPosition == null) {
            return;
        }

        int attempts = 5;
        while (attempts > 0) {
            int row = ThreadLocalRandom.current().nextInt(-creature.getSpeed(), creature.getSpeed() + 1);
            int col = ThreadLocalRandom.current().nextInt(-creature.getSpeed(), creature.getSpeed() + 1);
            Coordinates newPosition = new Coordinates(currentPosition.getX() + col, currentPosition.getY() + row);

            if (map.isWithinBounds(newPosition) && map.isPositionEmpty(newPosition)) {
                map.removeEntity(currentPosition, creature);
                map.addEntity(newPosition, creature);
                break;
            }
            attempts--;
        }
    }
}