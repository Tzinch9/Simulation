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

public class Renderer {

    private static final String RABBIT_SPRITE = "\uD83D\uDC30";
    private static final String FOX_SPRITE = "\uD83E\uDD8A";
    private static final String GRASS_SPRITE = "\uD83C\uDF31";
    private static final String MUSHROOM_SPRITE = "\uD83C\uDF44";
    private static final String MOUNTAIN_SPRITE = "⛰\uFE0F";
    private static final String ROCK_SPRITE = "\uD83E\uDEA8";
    private static final String TREE_SPRITE = "\uD83C\uDF33";
    private static final String BEAR_SPRITE = "🐻";
    private static final String RACCOON_SPRITE = "🦝";
    private static final String EMPTY_SPRITE = ".";

    public void render(GameMap map, int turnCount) {
        String[][] display = new String[map.getWidth()][map.getHeight()];
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                display[x][y] = EMPTY_SPRITE;
            }
        }

        for (var entry : map.getEntities().entrySet()) {
            Coordinates position = entry.getKey();
            Entity entity = entry.getValue();
            String sprite = getSpriteForEntity(entity);
            display[position.getX()][position.getY()] = sprite;
        }

        System.out.println();
        for (int y = 0; y < map.getHeight(); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < map.getWidth(); x++) {
                row.append(String.format("%-2s", display[x][y]));
                if (x < map.getWidth() - 1) {
                    row.append(' ');
                }
            }
            System.out.println(row);
        }
        System.out.println();
    }

    private String getSpriteForEntity(Entity entity) {
        if (entity instanceof Rabbit) return RABBIT_SPRITE;
        if (entity instanceof Raccoon) return RACCOON_SPRITE;
        if (entity instanceof Fox) return FOX_SPRITE;
        if (entity instanceof Bear) return BEAR_SPRITE;
        if (entity instanceof Grass) return GRASS_SPRITE;
        if (entity instanceof Mushroom) return MUSHROOM_SPRITE;
        if (entity instanceof Mountain) return MOUNTAIN_SPRITE;
        if (entity instanceof Rock) return ROCK_SPRITE;
        if (entity instanceof Tree) return TREE_SPRITE;
        return EMPTY_SPRITE;
    }
}