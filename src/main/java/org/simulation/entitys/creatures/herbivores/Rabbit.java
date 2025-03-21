    package org.simulation.entitys.creatures.herbivores;

    import org.simulation.dto.Coordinates;
    import org.simulation.dto.EntityFactory;
    import org.simulation.dto.EntityType;

    public class Rabbit extends Herbivore {

        public Rabbit(Coordinates position, int health, int speed, EntityFactory entityFactory) {
            super(position, health, speed, entityFactory);
        }

        @Override
        public EntityType getEntityType() {
            return EntityType.RABBIT;
        }
    }