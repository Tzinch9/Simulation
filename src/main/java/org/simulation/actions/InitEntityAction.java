package org.simulation.actions;

import org.simulation.dto.EntityFactory;
import org.simulation.dto.EntityType;
import org.simulation.dto.GameMap;
import org.simulation.entitys.Entity;

public class InitEntityAction implements Action {

    private final EntityFactory entityFactory;

    public InitEntityAction(EntityFactory entityFactory) {
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

        int totalCells = map.getWidth() * map.getHeight();

        int herbivoresCount = totalCells / 10; // Травоядные
        int predatorsCount = totalCells / 10; // Хищники
        int ediblesCount = totalCells / 10;   // Съедобные объекты
        int inEdiblesCount = totalCells / 10; // Несъедобные объекты

        int rabbitsCount = herbivoresCount / 2;
        int raccoonsCount = herbivoresCount - rabbitsCount;
        spawnEntity(EntityType.RABBIT, rabbitsCount, map);
        spawnEntity(EntityType.RACCOON, raccoonsCount, map);

        int foxesCount = predatorsCount / 2;
        int bearsCount = predatorsCount - foxesCount;
        spawnEntity(EntityType.FOX, foxesCount, map);
        spawnEntity(EntityType.BEAR, bearsCount, map);

        int grassCount = ediblesCount / 2;
        int mushroomCount = ediblesCount - grassCount;
        spawnEntity(EntityType.GRASS, grassCount, map);
        spawnEntity(EntityType.MUSHROOM, mushroomCount, map);

        int inEdiblePerType = inEdiblesCount / 3;
        int remainingInEdibles = inEdiblesCount - (inEdiblePerType * 3);
        spawnEntity(EntityType.ROCK, inEdiblePerType + (remainingInEdibles > 0 ? 1 : 0), map);
        spawnEntity(EntityType.TREE, inEdiblePerType + (remainingInEdibles > 1 ? 1 : 0), map);
        spawnEntity(EntityType.MOUNTAIN, inEdiblePerType, map);
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
                e.printStackTrace();
                break;
            }
        }
    }
}