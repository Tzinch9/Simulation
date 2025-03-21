package org.simulation.dto;

import org.simulation.entitys.Entity;
import org.simulation.entitys.creatures.herbivores.Rabbit;
import org.simulation.entitys.creatures.herbivores.Raccoon;
import org.simulation.entitys.creatures.predators.Bear;
import org.simulation.entitys.creatures.predators.Fox;
import org.simulation.entitys.environment.edible.Grass;
import org.simulation.entitys.environment.edible.Mushroom;
import org.simulation.entitys.environment.inedible.Mountain;
import org.simulation.entitys.environment.inedible.Rock;
import org.simulation.entitys.environment.inedible.Tree;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EntityFactory {
    private final SimulationConfig configurer;

    public EntityFactory(SimulationConfig configurer) {
        if (configurer == null) {
            throw new IllegalArgumentException("SimulationConfig не может быть null");
        }
        this.configurer = configurer;
    }

    public Entity createEntity(EntityType type, GameMap map) {
        if (type == null || map == null) {
            throw new IllegalArgumentException("EntityType и GameMap не могут быть null");
        }

        Coordinates position = map.getUnoccupiedRandomPosition();
        if (position == null) {
            return null;
        }

        return createEntityAtPosition(type, position);
    }

    public Entity createEntityAtPosition(EntityType type, Coordinates position) {
        if (type == null || position == null) {
            throw new IllegalArgumentException("EntityType и Coordinates не могут быть null");
        }

        Map<String, Integer> parameters = configurer.getParameters(type);
        return switch (type) {
            case RABBIT -> {
                checkParameters(parameters, "health", "speed");
                yield new Rabbit(position, parameters.get("health"), parameters.get("speed"), this);
            }
            case RACCOON -> {
                checkParameters(parameters, "health", "speed");
                yield new Raccoon(position, parameters.get("health"), parameters.get("speed"), this);
            }
            case FOX -> {
                checkParameters(parameters, "health", "speed", "attackPower");
                yield new Fox(position, parameters.get("health"), parameters.get("speed"), parameters.get("attackPower"), this);
            }
            case BEAR -> {
                checkParameters(parameters, "health", "speed", "attackPower");
                yield new Bear(position, parameters.get("health"), parameters.get("speed"), parameters.get("attackPower"), this);
            }
            case GRASS -> {
                checkParameters(parameters, "edibility");
                yield new Grass(position, parameters.get("edibility"));
            }
            case MUSHROOM -> {
                checkParameters(parameters, "edibility");
                yield new Mushroom(position, parameters.get("edibility"));
            }
            case ROCK -> new Rock(position);
            case TREE -> new Tree(position);
            case MOUNTAIN -> new Mountain(position);
        };
    }

    private void checkParameters(Map<String, Integer> parameters, String... requiredKeys) {
        for (String key : requiredKeys) {
            if (!parameters.containsKey(key)) {
                throw new IllegalStateException("Отсутствует обязательный параметр: " + key + " для создания объекта");
            }
        }
    }

    public Entity createRandomEdible(GameMap map) {
        if (map == null) {
            throw new IllegalArgumentException("GameMap не может быть null");
        }

        EntityType[] edibleTypes = {EntityType.GRASS, EntityType.MUSHROOM};
        EntityType randomType = edibleTypes[ThreadLocalRandom.current().nextInt(edibleTypes.length)];
        return createEntity(randomType, map);
    }
}