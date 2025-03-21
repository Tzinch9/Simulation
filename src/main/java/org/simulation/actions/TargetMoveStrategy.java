package org.simulation.actions;

import org.simulation.dto.Coordinates;
import org.simulation.dto.GameMap;
import org.simulation.dto.PathFinder;
import org.simulation.entitys.Entity;
import org.simulation.entitys.creatures.Creature;

import java.util.List;

public class    TargetMoveStrategy implements MoveStrategy {
    private final PathFinder pathFinder;

    public TargetMoveStrategy() {
        this.pathFinder = new PathFinder();
    }

    @Override
    public void move(Creature creature, GameMap map) {
        Coordinates currentPosition = creature.getPosition();
        if (currentPosition == null) {
            return;
        }

        // Проверяем соседние клетки на наличие цели
        Coordinates targetPosition = creature.findNeighbourTarget(map, currentPosition);
        if (targetPosition != null) {
            Entity target = map.getEntity(targetPosition);
            creature.interactWithTarget(target, map);
            return;
        }

        // Ищем ближайшую цель в радиусе
        targetPosition = map.findNearestEntity(currentPosition, creature.getTargetType(), creature.getSpeed() * 10);
        if (targetPosition != null) {
            List<Coordinates> path = pathFinder.findPath(currentPosition, targetPosition, map);
            if (path != null && path.size() > 1) {
                Coordinates nextPosition = path.get(1);
                if (map.isWithinBounds(nextPosition) && map.isPositionEmpty(nextPosition)) {
                    map.removeEntity(currentPosition, creature);
                    map.addEntity(nextPosition, creature);
                }
            }
        }
    }
}