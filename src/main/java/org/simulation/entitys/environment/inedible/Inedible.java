package org.simulation.entitys.environment.inedible;

import org.simulation.dto.Coordinates;
import org.simulation.entitys.environment.Environment;

public abstract class Inedible extends Environment {

    public Inedible(Coordinates position) {
        super(position, false);
    }
}
