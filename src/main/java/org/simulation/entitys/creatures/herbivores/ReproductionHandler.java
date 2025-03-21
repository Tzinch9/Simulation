package org.simulation.entitys.creatures.herbivores;

import org.simulation.dto.Coordinates;
import org.simulation.dto.EntityFactory;
import org.simulation.dto.GameMap;
import org.simulation.entitys.Entity;
import org.simulation.entitys.creatures.Creature;

public class ReproductionHandler {
    private final EntityFactory entityFactory;
    private static final int REPRODUCTION_COOLDOWN = 10; // Увеличиваем с 5 до 10

    public ReproductionHandler(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public void attemptReproduction(Creature creature, GameMap map, int turnsSinceLastReproduction) {
        if (turnsSinceLastReproduction < REPRODUCTION_COOLDOWN) {
            return;
        }

        Coordinates currentPosition = creature.getPosition();
        if (currentPosition == null) {
            return;
        }

        Coordinates partnerPosition = findNeighbourPartner(creature, map, currentPosition);
        if (partnerPosition == null) {
            return;
        }

        Coordinates newPosition = findFreeNeighbour(map, currentPosition);
        if (newPosition == null) {
            return;
        }

        Entity newEntity = entityFactory.createEntityAtPosition(creature.getEntityType(), newPosition);
        if (newEntity != null) {
            map.addEntity(newPosition, newEntity);
            creature.resetReproductionCooldown();
        }
    }

    private Coordinates findNeighbourPartner(Creature creature, GameMap map, Coordinates currentPosition) {
        int[] col = {0, 1, 0, -1};
        int[] row = {1, 0, -1, 0};

        for (int i = 0; i < 4; i++) {
            Coordinates neighbour = new Coordinates(currentPosition.getX() + col[i], currentPosition.getY() + row[i]);
            if (map.isWithinBounds(neighbour) && !map.isPositionEmpty(neighbour)) {
                Entity entity = map.getEntity(neighbour);
                if (creature.getClass().isInstance(entity)) {
                    return neighbour;
                }
            }
        }
        return null;
    }

    private Coordinates findFreeNeighbour(GameMap map, Coordinates currentPosition) {
        int[] col = {0, 1, 0, -1};
        int[] row = {1, 0, -1, 0};

        for (int i = 0; i < 4; i++) {
            Coordinates neighbour = new Coordinates(currentPosition.getX() + col[i], currentPosition.getY() + row[i]);
            if (map.isWithinBounds(neighbour) && map.isPositionEmpty(neighbour)) {
                return neighbour;
            }
        }
        return null;
    }
}