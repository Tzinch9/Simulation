package org.simulation.dto;

import org.simulation.actions.Action;
import org.simulation.actions.GrowEdibleAction;
import org.simulation.actions.InitEntityAction;
import org.simulation.actions.TurnAction;
import org.simulation.entitys.creatures.Creature;
import org.simulation.entitys.creatures.herbivores.Rabbit;
import org.simulation.entitys.creatures.herbivores.Raccoon;
import org.simulation.entitys.creatures.predators.Fox;
import org.simulation.entitys.creatures.predators.Bear;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Simulation {

    private final GameMap gameMap;
    private final EntityFactory entityFactory;
    private final Renderer renderer;
    private final List<Action> initAction;
    private final List<Action> turnAction;
    private int turnCount;
    private final Scanner scanner;

    public Simulation() {
        this.scanner = new Scanner(System.in);
        SimulationConfig config = new SimulationConfig();
        this.gameMap = new GameMap(config.getMapWidth(), config.getMapHeight());
        this.entityFactory = new EntityFactory(config);
        this.renderer = new Renderer();
        this.initAction = new ArrayList<>();
        this.turnAction = new ArrayList<>();
        this.turnCount = 0;

        // Инициализация действий
        this.addInitAction(new InitEntityAction(entityFactory));
        this.addTurnAction(new TurnAction());
        this.addTurnAction(new GrowEdibleAction(entityFactory));
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void addTurnAction(Action action) {
        turnAction.add(action);
    }

    public void addInitAction(Action action) {
        initAction.add(action);
    }

    public boolean nextTurn() {
        turnCount++;
        System.out.println("Ход " + turnCount);
        for (Action action : turnAction) {
            action.execute(gameMap);
        }
        renderer.render(gameMap, turnCount);

        long herbivoreCount = gameMap.getEntities().values().stream()
                .filter(entity -> entity instanceof Rabbit || entity instanceof Raccoon)
                .count();
        long predatorCount = gameMap.getEntities().values().stream()
                .filter(entity -> entity instanceof Fox || entity instanceof Bear)
                .count();

        if (herbivoreCount == 0 || predatorCount == 0) {
            String winningPopulation = herbivoreCount == 0 ? "Хищники" : "Травоядные";
            String dominantSpecies = determineDominantSpecies(winningPopulation);
            System.out.println("Симуляция завершена! Выжила популяция: " + winningPopulation);
            System.out.println("Доминирующий вид: " + dominantSpecies);
            return false;
        }
        return true;
    }

    private String determineDominantSpecies(String winningPopulation) {
        Map<Class<?>, Long> speciesCount = gameMap.getEntities().values().stream()
                .filter(entity -> entity instanceof Creature)
                .collect(Collectors.groupingBy(
                        Object::getClass,
                        Collectors.counting()
                ));

        if (winningPopulation.equals("Травоядные")) {
            long rabbitCount = speciesCount.getOrDefault(Rabbit.class, 0L);
            long raccoonCount = speciesCount.getOrDefault(Raccoon.class, 0L);
            return rabbitCount >= raccoonCount ? "Кролик" : "Енот";
        } else {
            long foxCount = speciesCount.getOrDefault(Fox.class, 0L);
            long bearCount = speciesCount.getOrDefault(Bear.class, 0L);
            return foxCount >= bearCount ? "Лиса" : "Медведь";
        }
    }

    public void startSimulation() {
        for (Action action : initAction) {
            action.execute(gameMap);
        }
        renderer.render(gameMap, turnCount);

        boolean running = true;
        while (running) {
            running = nextTurn();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Симуляция прервана.");
                break;
            }
        }
    }

    public void start() {
        boolean continueProgram = true;

        while (continueProgram) {
            startSimulation();

            System.out.println("Хотите начать новую симуляцию с новыми параметрами? (да/нет)");
            while (true) {
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("да")) {
                    Simulation newSimulation = new Simulation();
                    newSimulation.startSimulation();
                    break;
                } else if (response.equals("нет")) {
                    continueProgram = false;
                    break;
                } else {
                    System.out.println("Пожалуйста, введите 'да' для новой симуляции или 'нет' для выхода.");
                }
            }
        }

        System.out.println("Программа завершена.");
        scanner.close();
    }
}