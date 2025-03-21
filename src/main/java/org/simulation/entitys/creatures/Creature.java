package org.simulation.entitys.creatures;

import org.simulation.actions.MoveStrategy;
import org.simulation.actions.ReproductionStrategy;
import org.simulation.actions.StandardReproductionStrategy;
import org.simulation.actions.TargetMoveStrategy;
import org.simulation.dto.*;
import org.simulation.entitys.Entity;


import java.util.function.Predicate;

abstract public class Creature extends Entity {

    private int health;
    private int speed;
    private int turnsSinceLastReproduction;
    private final EntityFactory entityFactory;
    private final MoveStrategy moveStrategy;
    private final ReproductionStrategy reproductionStrategy;

    protected Creature(Coordinates position, int health, int speed, EntityFactory entityFactory) {
        super(position);
        this.health = health;
        this.speed = speed;
        this.entityFactory = entityFactory;
        this.moveStrategy = new TargetMoveStrategy();
        this.reproductionStrategy = createReproductionStrategy();
        this.turnsSinceLastReproduction = 0;
    }

    protected ReproductionStrategy createReproductionStrategy() {
        return new StandardReproductionStrategy(entityFactory);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health < 0) {
            throw new IllegalArgumentException("Здоровье не может быть отрицательным");
        }
        this.health = health;
    }

    public int getSpeed() {
        if (speed <= 0) {
            throw new IllegalArgumentException("Скорость должна быть положительной");
        }
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void resetReproductionCooldown() {
        this.turnsSinceLastReproduction = 0;
    }

    public abstract Class<? extends Entity> getTargetType();

    public abstract void interactWithTarget(Entity target, GameMap map);

    public abstract EntityType getEntityType();

    @Override
    public void makeMove(GameMap map) {
        Coordinates currentPosition = getPosition();
        if (currentPosition == null) {
            return;
        }

        if (tryInteractWithNeighbourTarget(map, currentPosition)) {
            return;
        }

        move(map);
        turnsSinceLastReproduction++;
    }

    public void reproduce(GameMap map, ReproductionStrategy strategy) {
        strategy.attemptReproduction(this, map, turnsSinceLastReproduction);
    }

    private boolean tryInteractWithNeighbourTarget(GameMap map, Coordinates currentPosition) {
        Coordinates targetPosition = findNeighbourTarget(map, currentPosition);
        if (targetPosition != null) {
            Entity target = map.getEntity(targetPosition);
            interactWithTarget(target, map);
            return true;
        }
        return false;
    }

    private void move(GameMap map) {
        moveStrategy.move(this, map);
    }

    public Coordinates findNeighbourTarget(GameMap map, Coordinates currentPosition) {
        Class<? extends Entity> targetType = getTargetType();
        if (targetType == null) {
            throw new IllegalStateException("targetType не может быть null для " + this.getClass().getSimpleName());
        }
        Predicate<Entity> isTarget = entity -> entity != null && targetType.isInstance(entity);
        return NeighbourFinder.findNeighbour(map, currentPosition, isTarget);
    }
}