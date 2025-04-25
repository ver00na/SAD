package university;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AntColonyOptimization {
    // Граф, представленный в виде матрицы расстояний между узлами
    private final double[][] graph;

    // Параметры алгоритма
    private final int nAnts; // Количество муравьев
    private final int nIterations; // Число итераций алгоритма
    private final double alpha; // Влияние феромонов
    private final double beta; // Влияние расстояния
    private final double rho; // Коэффициент испарения феромонов
    private final double Q; // Коэффициент усиления феромонов

    // Матрица феромонов
    private double[][] pheromone;

    // Лучший найденный маршрут и его длина
    private List<Integer> bestPath;
    private double bestDistance;

    // Конструктор класса
    public AntColonyOptimization(double[][] graph, int nAnts, int nIterations, double alpha, double beta, double rho, double Q) {
        this.graph = graph;
        this.nAnts = nAnts;
        this.nIterations = nIterations;
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.Q = Q;

        // Инициализация матрицы феромонов
        this.pheromone = new double[graph.length][graph.length];
        for (double[] row : pheromone) {
            Arrays.fill(row, 1.0 / graph.length); // Изначально равномерно распределяем феромоны
        }

        // Устанавливаем начальные значения для лучшего маршрута
        this.bestDistance = Double.MAX_VALUE;
    }

    // Основной метод запуска алгоритма
    public void run() {
        for (int iteration = 0; iteration < nIterations; iteration++) {
            System.out.printf("\nИтерация %d/%d%n", iteration + 1, nIterations);

            // Динамическое изменение параметров alpha и beta
            double alphaDynamic = alpha * (1 - ((double) iteration / nIterations)); // Влияние феромонов уменьшается
            double betaDynamic = beta + ((double) iteration / nIterations); // Влияние расстояний увеличивается

            // Построение решений для всех муравьев
            List<Path> allPaths = constructSolutions(alphaDynamic, betaDynamic);

            // Обновление матрицы феромонов на основе пройденных маршрутов
            updatePheromones(allPaths);
        }
    }

    // Построение маршрутов для всех муравьев
    private List<Path> constructSolutions(double alphaDynamic, double betaDynamic) {
        List<Path> allPaths = new ArrayList<>();
        for (int ant = 0; ant < nAnts; ant++) {
            // Каждый муравей строит свой маршрут
            Path path = constructSinglePath(alphaDynamic, betaDynamic);

            // Вывод маршрута текущего муравья
            System.out.printf("  Муравей %d: %s, Путь: %.2f%n", ant + 1, path.nodes, path.distance);

            // Добавляем маршрут в общий список
            allPaths.add(path);

            // Обновляем лучший найденный маршрут, если он короче текущего
            if (path.distance < bestDistance) {
                bestDistance = path.distance;
                bestPath = path.nodes;
            }
        }
        return allPaths;
    }

    // Построение маршрута для одного муравья
    private Path constructSinglePath(double alphaDynamic, double betaDynamic) {
        List<Integer> path = new ArrayList<>();
        path.add(0); // Начинаем маршрут с узла 0 (первая вершина)
        double totalDistance = 0; // Общая длина маршрута
        boolean[] visited = new boolean[graph.length]; // Массив посещенных узлов
        visited[0] = true;

        // Пока не посетили все узлы
        while (path.size() < graph.length) {
            int currentNode = path.get(path.size() - 1);

            // Выбираем следующий узел
            Integer nextNode = selectNextNode(currentNode, visited, alphaDynamic, betaDynamic);
            if (nextNode == null) break; // Если нет доступных узлов, выходим

            path.add(nextNode); // Добавляем узел в маршрут
            visited[nextNode] = true; // Отмечаем узел как посещенный
            totalDistance += graph[currentNode][nextNode]; // Увеличиваем длину маршрута
        }

        // Возвращаемся в начальный узел, если маршрут завершен
        if (path.size() == graph.length) {
            totalDistance += graph[path.get(path.size() - 1)][0];
            path.add(0); // Замыкаем цикл
        }

        return new Path(path, totalDistance);
    }

    // Выбор следующего узла на основе вероятностей
    private Integer selectNextNode(int currentNode, boolean[] visited, double alphaDynamic, double betaDynamic) {
        double[] probabilities = new double[graph.length];
        double totalProbability = 0;

        // Рассчитываем вероятность перехода в каждый узел
        for (int neighbor = 0; neighbor < graph.length; neighbor++) {
            if (!visited[neighbor]) { // Если узел не посещен
                double pheromoneWeight = Math.pow(pheromone[currentNode][neighbor], alphaDynamic);
                double distanceWeight = Math.pow(1.0 / graph[currentNode][neighbor], betaDynamic);
                probabilities[neighbor] = pheromoneWeight * distanceWeight; // Учет влияния феромонов и расстояния
                totalProbability += probabilities[neighbor];
            }
        }

        if (totalProbability == 0) return null; // Если нет доступных узлов

        // Нормализация вероятностей
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= totalProbability;
        }

        // Выбор узла на основе вероятностей
        return selectNodeByProbability(probabilities);
    }

    // Выбор узла на основе нормализованных вероятностей
    private int selectNodeByProbability(double[] probabilities) {
        double random = new Random().nextDouble();
        double cumulative = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            cumulative += probabilities[i];
            if (random <= cumulative) return i;
        }

        return -1; // На случай ошибки (не должно происходить)
    }

    // Обновление матрицы феромонов
    private void updatePheromones(List<Path> allPaths) {
        // Испарение феромонов
        for (int i = 0; i < pheromone.length; i++) {
            for (int j = 0; j < pheromone[i].length; j++) {
                pheromone[i][j] *= (1 - rho); // Уменьшаем уровень феромонов
            }
        }

        // Усиление феромонов на пройденных маршрутах
        for (Path path : allPaths) {
            for (int i = 0; i < path.nodes.size() - 1; i++) {
                int from = path.nodes.get(i);
                int to = path.nodes.get(i + 1);
                pheromone[from][to] += Q / path.distance; // Усиливаем феромоны
                pheromone[to][from] += Q / path.distance;
            }
        }

        // Дополнительное усиление феромонов на лучшем маршруте
        for (int i = 0; i < bestPath.size() - 1; i++) {
            int from = bestPath.get(i);
            int to = bestPath.get(i + 1);
            pheromone[from][to] += (10 * Q) / bestDistance; // Усиление лучшего маршрута
            pheromone[to][from] += (10 * Q) / bestDistance;
        }
    }

    // Получение лучшего маршрута
    public List<Integer> getBestPath() {
        return bestPath;
    }

    // Получение длины лучшего маршрута
    public double getBestDistance() {
        return bestDistance;
    }

    // Вспомогательный класс для хранения пути и его длины
    private static class Path {
        List<Integer> nodes;
        double distance;

        Path(List<Integer> nodes, double distance) {
            this.nodes = nodes;
            this.distance = distance;
        }
    }

    // Точка входа в программу
    public static void main(String[] args) {
        // Определяем граф (матрица расстояний)
        double[][] graph = {
                {0, 2, 3, 5},
                {2, 0, 6, 3},
                {3, 6, 0, 7},
                {5, 3, 7, 0}
        };

        // Создаем объект класса и запускаем алгоритм
        AntColonyOptimization aco = new AntColonyOptimization(graph, 4, 200, 1, 2, 0.05, 4);
        aco.run();

        // Выводим лучший найденный маршрут и его длину
        System.out.println("\nЛучший путь: " + aco.getBestPath());
        System.out.println("Лучшая длина пути: " + aco.getBestDistance());
    }
}
