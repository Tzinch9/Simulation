package org.simulation.actions;

import org.simulation.dto.EntityFactory;
import org.simulation.dto.EntityType;
import org.simulation.dto.GameMap;
import org.simulation.entitys.Entity;
import org.simulation.entitys.environment.edible.Edible;

public class GrowEdibleAction implements Action {   // рост съедобных объектов

    private final EntityFactory entityFactory;
    private final EntityType[] edibleTypes = {EntityType.MUSHROOM, EntityType.GRASS};

    public GrowEdibleAction(EntityFactory entityFactory) {
        if (entityFactory == null) {
            throw new IllegalArgumentException("EntityFactory cannot be null");
        }
        this.entityFactory = entityFactory;
    }

    @Override
    public void execute(GameMap map) {
        if (map == null) {
            throw new IllegalArgumentException("GameMap cannot be null");
        }
        boolean hasEdible = map.getEntities().values().stream()
                .anyMatch(entity -> entity instanceof Edible);
        if (hasEdible) {
            return;
        }

        int totalCells = map.getWidth() * map.getHeight();
        long occupiedCells = map.getEntities().size();
        int freeCells = totalCells - (int) occupiedCells;

        int edibleCount = freeCells / 10;
        if (edibleCount == 0) {
            return;
        }

        int grassCount = edibleCount / 2;
        int mushroomCount = edibleCount - grassCount;

        spawnEntity(EntityType.GRASS, grassCount, map);

        spawnEntity(EntityType.MUSHROOM, mushroomCount, map);
    }

    private void spawnEntity(EntityType entityType, int count, GameMap map) {
        int successfulSpawns = 0;
        while (successfulSpawns < count) {
            try {
                Entity entity = entityFactory.createEntity(entityType, map);
                if (entity != null) {
                    map.addEntity(entity.getPosition(), entity);
                    successfulSpawns++;
                } else {
                    break;
                }
            } catch (Exception e) {
                break;
            }
        }
    }
}