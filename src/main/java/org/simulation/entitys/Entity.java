package org.simulation.entitys;

import org.simulation.dto.Coordinates;
import org.simulation.dto.GameMap;

abstract public class Entity {

    private Coordinates position;

    public Entity(Coordinates position) {
        this.position = position;
    }

    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public abstract void makeMove(GameMap map);
}
