package org.simulation.entitys.environment.edible;

import org.simulation.dto.Coordinates;
import org.simulation.entitys.environment.Environment;

public abstract class Edible extends Environment {

    protected int edibility;

    public Edible(Coordinates position,int edibility) {
        super(position, true);
        this.edibility = edibility;
    }

    public int getEdibility() {
        return edibility;
    }



}
