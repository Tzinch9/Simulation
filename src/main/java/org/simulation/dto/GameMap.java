package org.simulation.dto;

import org.simulation.entitys.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameMap {

    private int width;
    private int height;
    private Map<Coordinates, Entity> entities;
    private Random random = new Random();

    public GameMap(int width, int height) {

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be greater than 0");
        }
        this.width = width;
        this.height = height;
        entities = new HashMap<>(width * height);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isPositionEmpty(Coordinates position) {      // позиция пустая?
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (!isWithinBounds(position)) {
            throw new IllegalArgumentException("Position is not within bounds");
        }
        return !entities.containsKey(position);
    }

    public void addEntity(Coordinates position, Entity entity) {    // добавить сущность
        if (position == null || entity == null) {
            throw new IllegalArgumentException("Position and entity cannot be null");
        }
        if (!isWithinBounds(position)) {
            throw new IllegalArgumentException("Position is not within bounds");
        }
        if (!isPositionEmpty(position)) {
            throw new IllegalArgumentException("Position is already occupied");
        }
        entities.put(position, entity);
        entity.setPosition(position);

    }

    public Entity getEntity(Coordinates position) {     // дай сущность
        if (position == null){
            throw new IllegalArgumentException("Position cannot be null");
        }

        if (!isWithinBounds(position)) {
            throw new IllegalArgumentException("Position is not within bounds");
        }
        return this.entities.get(position);
    }

    public void removeEntity(Coordinates position, Entity entity) {    // удали сущность
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (isPositionEmpty(position)) {
            return;
        }

        if (!isWithinBounds(position)) {
            throw new IllegalArgumentException("Position is not within bounds");
        }
        entities.remove(position);
        entity.setPosition(null);

    }

    public Coordinates getUnoccupiedRandomPosition() {

        if (width <= 0 || height <= 0) {
            throw new IllegalStateException("width and height must be greater than 0");
        }

        int attempts = 0;
        final int maxAttempts = width * height;

        while (attempts < maxAttempts) {
            Coordinates position = getRandomPosition();
            if (isPositionEmpty(position)) {
                return position;
            }
            attempts++;
        }
        return null;
    }



    private Coordinates getRandomPosition() {
        if (width <= 0 || height <= 0) {
            throw new IllegalStateException("width and height must be greater than 0");
        }
        return new Coordinates(random.nextInt(width), random.nextInt(height));
    }

    public Map<Coordinates, Entity> getEntities() {
        return entities;
    }

    public boolean isWithinBounds(Coordinates position) {   // находится в пределах границы

        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        return position.getX() >= 0 && position.getX() < width && position.getY() >= 0 && position.getY() < height;
    }

    public Coordinates findNearestEntity(Coordinates from, Class<? extends Entity> targetType, int radius) {    //поиск ближайшего объекта
        if (from == null || targetType == null || radius < 0) {
            return null;
        }
        Coordinates nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<Coordinates, Entity> entry : entities.entrySet()) {
            Coordinates position = entry.getKey();
            Entity entity = entry.getValue();

            if (position.equals(from)) {
                continue;
            }
            if (targetType.isInstance(entity)) {
                double distance = calculateDistance(from, position);

                if (distance <= radius && distance < minDistance) {
                    minDistance = distance;
                    nearest = position;
                }
            }
        }
        return nearest;
    }

    private double calculateDistance(Coordinates from, Coordinates to) {
        int x = to.getX() - from.getX();
        int y = to.getY() - from.getY();
        return Math.sqrt(x * x + y * y);
    }
}
