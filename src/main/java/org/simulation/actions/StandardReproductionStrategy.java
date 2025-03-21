package org.simulation.actions;

import org.simulation.dto.Coordinates;
import org.simulation.dto.EntityFactory;
import org.simulation.dto.GameMap;
import org.simulation.dto.NeighbourFinder;
import org.simulation.entitys.Entity;
import org.simulation.entitys.creatures.Creature;


import java.util.function.Predicate;

public class StandardReproductionStrategy implements ReproductionStrategy {
    private final EntityFactory entityFactory;
    private static final int TURNS_BETWEEN_REPRODUCTION = 10; // ходы между размножениями

    public StandardReproductionStrategy(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    @Override
    public void attemptReproduction(Creature creature, GameMap map, int turnsSinceLastReproduction) {
        if (turnsSinceLastReproduction < TURNS_BETWEEN_REPRODUCTION) {
            return;
        }

        Coordinates currentPosition = creature.getPosition();
        if (currentPosition == null) {
            return;
        }

        Coordinates partnerPosition = findPartner(creature, map, currentPosition);
        if (partnerPosition == null) {
            return;
        }

        Coordinates newPosition = findFreeSpace(map, currentPosition);
        if (newPosition == null) {
            return;
        }

        spawnChild(creature, map, newPosition);
    }

    private Coordinates findPartner(Creature creature, GameMap map, Coordinates currentPosition) {
        Predicate<Entity> isPartner = entity -> entity != null && creature.getClass().isInstance(entity);
        return NeighbourFinder.findNeighbour(map, currentPosition, isPartner);
    }

    private Coordinates findFreeSpace(GameMap map, Coordinates currentPosition) {
        Predicate<Entity> isEmpty = entity -> entity == null;
        return NeighbourFinder.findNeighbour(map, currentPosition, isEmpty);
    }

    private void spawnChild(Creature creature, GameMap map, Coordinates newPosition) {
        try {
            Entity newEntity = entityFactory.createEntityAtPosition(creature.getEntityType(), newPosition);
            if (newEntity != null) {
                map.addEntity(newPosition, newEntity);
                creature.resetReproductionCooldown();
            }
        } catch (Exception e) {
            System.out.println("Ошибка при создании нового существа: " + e.getMessage());
        }
    }
}