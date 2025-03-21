package org.simulation.entitys.creatures.predators;

import org.simulation.dto.Coordinates;
import org.simulation.dto.EntityFactory;
import org.simulation.dto.GameMap;
import org.simulation.entitys.Entity;
import org.simulation.entitys.creatures.Creature;
import org.simulation.entitys.creatures.herbivores.Herbivore;

public abstract class Predator extends Creature {

    private final int attackPower;
    private int hunger;

    protected Predator(Coordinates position, int health, int speed, int attackPower, EntityFactory entityFactory) {
        super(position, health, speed, entityFactory);
        this.attackPower = attackPower;
        this.hunger = 0;
    }

    public int getAttackPower() {
        return attackPower;
    }

    @Override
    public Class<? extends Entity> getTargetType() {
        return Herbivore.class;
    }

    @Override
    public void interactWithTarget(Entity target, GameMap map) {
        if (target instanceof Herbivore herbivore) {
            int initialHealth = herbivore.getHealth();
            int damageDealt = Math.min(initialHealth, getAttackPower());
            herbivore.setHealth(initialHealth - damageDealt);
            setHealth(getHealth() + damageDealt);
            if (herbivore.getHealth() <= 0) {
                map.removeEntity(target.getPosition(), target);
                this.hunger = 0;
            }
        }
    }

    @Override
    public void makeMove(GameMap map) {
        super.makeMove(map);
        hunger++;
    }
}