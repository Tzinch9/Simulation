package org.simulation.entitys.environment;

import org.simulation.dto.Coordinates;
import org.simulation.dto.GameMap;
import org.simulation.entitys.Entity;

public class Environment extends Entity {

    private boolean isEdible;

    public Environment(Coordinates position, boolean isEdible) {
        super(position);
        this.isEdible = isEdible;
    }

    public boolean isEdible() {
        return isEdible;
    }

    @Override
    public void makeMove(GameMap map) {
    }
}
