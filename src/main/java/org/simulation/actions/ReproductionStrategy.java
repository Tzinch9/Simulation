package org.simulation.actions;

import org.simulation.dto.GameMap;
import org.simulation.entitys.creatures.Creature;

public interface ReproductionStrategy {
    void attemptReproduction(Creature creature, GameMap map, int turnsSinceLastReproduction);
}