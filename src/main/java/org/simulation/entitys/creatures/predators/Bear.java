package org.simulation.entitys.creatures.predators;

import org.simulation.dto.Coordinates;
import org.simulation.dto.EntityFactory;
import org.simulation.dto.EntityType;

public class Bear extends Predator {

    public Bear(Coordinates position, int health, int speed, int attackPower, EntityFactory entityFactory) {
        super(position, health, speed, attackPower, entityFactory);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.BEAR;
    }
}