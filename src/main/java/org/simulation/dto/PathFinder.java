package org.simulation.dto;

import java.util.*;

public class PathFinder {

    class Node {
        public Coordinates coordinates;             // координаты
        public double costOfThePathFromTheStart;    // стоимость пути от начала
        public double heuristicsToTheTarget;        // эвристика
        public double totalCost;                    // общая стоимость
        public Node parentNode;                     // родительская нода

        public Node(Coordinates coordinates, double costOfThePathFromTheStart, double heuristicsToTheTarget, Node parentNode) {
            this.coordinates = coordinates;
            this.costOfThePathFromTheStart = costOfThePathFromTheStart;
            this.heuristicsToTheTarget = heuristicsToTheTarget;
            this.totalCost = costOfThePathFromTheStart + heuristicsToTheTarget;
            this.parentNode = parentNode;
        }
    }

    public List<Coordinates> findPath(Coordinates start, Coordinates target, GameMap map) {
        if (start.equals(target)) {
            return new ArrayList<>();
        }

                                                        // Открытый список
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(node -> node.totalCost));
        Map<Coordinates, Node> openSetMap = new HashMap<>();
                                                        // Закрытый список координат
        Set<Coordinates> closedSet = new HashSet<>();

        Node startNode = new Node(start, 0, heuristic(start, target), null);
        openSet.add(startNode);
        openSetMap.put(start, startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            openSetMap.remove(currentNode.coordinates);

            if (currentNode.coordinates.equals(target)) {
                return reconstructPath(currentNode);
            }

            closedSet.add(currentNode.coordinates);

            // 8 направлений включая диагонали
            Coordinates[] neighbors = {
                    new Coordinates(currentNode.coordinates.getX() + 1, currentNode.coordinates.getY()),     // вправо
                    new Coordinates(currentNode.coordinates.getX() - 1, currentNode.coordinates.getY()),     // влево
                    new Coordinates(currentNode.coordinates.getX(), currentNode.coordinates.getY() + 1),     // вверх
                    new Coordinates(currentNode.coordinates.getX(), currentNode.coordinates.getY() - 1),     // вниз
                    new Coordinates(currentNode.coordinates.getX() + 1, currentNode.coordinates.getY() + 1), // вправо-вверх
                    new Coordinates(currentNode.coordinates.getX() + 1, currentNode.coordinates.getY() - 1), // вправо-вниз
                    new Coordinates(currentNode.coordinates.getX() - 1, currentNode.coordinates.getY() + 1), // влево-вверх
                    new Coordinates(currentNode.coordinates.getX() - 1, currentNode.coordinates.getY() - 1)  // влево-вниз
            };

            for (Coordinates neighbor : neighbors) {
                if (!map.isWithinBounds(neighbor)) {
                    continue;
                }
                if (!map.isPositionEmpty(neighbor) && !neighbor.equals(target)) {
                    continue;
                }
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double costOfWay = currentNode.costOfThePathFromTheStart + distance(currentNode.coordinates, neighbor);

                Node neighborNode = openSetMap.get(neighbor);
                if (neighborNode == null) {
                    neighborNode = new Node(neighbor, costOfWay, heuristic(neighbor, target), currentNode);
                    openSet.add(neighborNode);
                    openSetMap.put(neighbor, neighborNode);
                } else if (costOfWay < neighborNode.costOfThePathFromTheStart) {
                    openSet.remove(neighborNode);
                    neighborNode.costOfThePathFromTheStart = costOfWay;
                    neighborNode.heuristicsToTheTarget = heuristic(neighbor, target);
                    neighborNode.totalCost = costOfWay + neighborNode.heuristicsToTheTarget;
                    neighborNode.parentNode = currentNode;
                    openSet.add(neighborNode);
                }
            }
        }
        return null;
    }

    private double heuristic(Coordinates start, Coordinates target) {
        return Math.abs(start.getX() - target.getX()) + Math.abs(start.getY() - target.getY());
    }

    private List<Coordinates> reconstructPath(Node node) {
        List<Coordinates> path = new ArrayList<>();
        while (node != null) {
            path.add(node.coordinates);
            node = node.parentNode;
        }
        Collections.reverse(path);
        return path;
    }

    private double distance(Coordinates start, Coordinates target) {
        if (start.getX() != target.getX() && start.getY() != target.getY()) {
            return Math.sqrt(2);
        }
        return 1;
    }
}