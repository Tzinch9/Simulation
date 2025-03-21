package org.simulation.actions;

import org.simulation.dto.Coordinates;
import org.simulation.dto.GameMap;
import org.simulation.entitys.creatures.Creature;

public interface MoveStrategy {
    void move(Creature creature, GameMap map);
}