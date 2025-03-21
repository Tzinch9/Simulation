package org.simulation.entitys.creatures.herbivores;

import org.simulation.dto.Coordinates;
import org.simulation.dto.EntityFactory;
import org.simulation.dto.GameMap;
import org.simulation.entitys.Entity;
import org.simulation.entitys.creatures.Creature;
import org.simulation.entitys.environment.edible.Edible;

public abstract class Herbivore extends Creature {

    private int hunger;

    public Herbivore(Coordinates position, int health, int speed, EntityFactory entityFactory) {
        super(position, health, speed, entityFactory);
        this.hunger = 0;
    }

    @Override
    public Class<? extends Entity> getTargetType() {
        return Edible.class;
    }

    @Override
    public void interactWithTarget(Entity target, GameMap map) {
        if (target instanceof Edible edible) {
            setHealth(getHealth() + edible.getEdibility());
            map.removeEntity(target.getPosition(), target);
            this.hunger = 0;
        }
    }

    @Override
    public void makeMove(GameMap map) {
        super.makeMove(map);
        hunger++;
    }
}