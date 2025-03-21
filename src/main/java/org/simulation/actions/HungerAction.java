package org.simulation.actions;

import org.simulation.dto.Coordinates;
import org.simulation.dto.GameMap;
import org.simulation.entitys.Entity;
import org.simulation.entitys.creatures.Creature;

import java.util.HashMap;
import java.util.Map;

public class HungerAction implements Action {       // эффект голода

    @Override
    public void execute(GameMap map) {
        if (map == null) {
            throw new IllegalArgumentException("Карта не может быть null");
        }

        Map<Coordinates, Entity> entities = new HashMap<>(map.getEntities());
        for (Map.Entry<Coordinates, Entity> entry : entities.entrySet()) {
            Entity entity = entry.getValue();
            if (entity instanceof Creature creature) {
                creature.setHealth(creature.getHealth() - 1);
                if (creature.getHealth() <= 0) {
                    map.removeEntity(entry.getKey(), entity);
                }
            }
        }
    }
}