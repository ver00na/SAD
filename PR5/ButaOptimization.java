package university;

import java.util.*;

public class ButaOptimization {

    static final int S = 5; // Количество агентов
    static final int N = 5; // Дополнительные агенты от лучшего агента
    static final int M = 5; // Дополнительные агенты от перспективного агента
    static final double delta = 1.0; // Могут изменять свои позиции при локальном поиске
    static final double threshold = 0.2; // Пороговое значение для расстояния между агентами
    static final int maxIterations = 1000;
    static final double xMin = -5.0, xMax = 5.0, yMin = -10.0, yMax = 10.0;

    // Функция Буты
    public static double butaFunction(double x, double y) {
        return Math.pow(x + 2 * y - 7, 2) + Math.pow(2 * x + y - 5, 2);
    }

    // Функция для вычисления евклидова расстояния
    public static double euclideanDistance(double[] point1, double[] point2) {
        return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
    }

    // Функция для вычисления середины между двумя точками
    public static double[] midpoint(double[] point1, double[] point2) {
        double xNew = (point1[0] + point2[0]) / 2;
        double yNew = (point1[1] + point2[1]) / 2;
        return new double[] { xNew, yNew };
    }

    // Локальный поиск
    public static double[] localSearch(double[] center, double delta) {
        double bestValue = butaFunction(center[0], center[1]);
        double[] bestPoint = center;
        System.out.printf("Локальный поиск для позиции (%.2f, %.2f): начальная оценка %.4f%n", center[0], center[1], bestValue);

        for (int i = 0; i < 4; i++) {
            double x = Math.max(xMin, Math.min(xMax, center[0] + Math.random() * 2 * delta - delta));
            double y = Math.max(yMin, Math.min(yMax, center[1] + Math.random() * 2 * delta - delta));
            double value = butaFunction(x, y);
            System.out.printf("Точка: (%.2f, %.2f), значение: %.4f%n", x, y, value);

            if (value < bestValue) {
                System.out.printf("Новая лучшая позиция: (%.2f, %.2f) с функцией %.4f%n", x, y, value);
                bestPoint = new double[] { x, y };
                bestValue = value;
            }
        }
        return bestPoint;
    }

    public static void main(String[] args) {
        Map<String, double[]> agents = new HashMap<>();
        Random rand = new Random();

        // Инициализация агентов
        for (int i = 0; i < S; i++) {
            double x = xMin + (xMax - xMin) * rand.nextDouble();
            double y = yMin + (yMax - yMin) * rand.nextDouble();
            agents.put("Шмелик" + (i + 1), new double[] { x, y });
        }

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            System.out.printf("%nИтерация %d%n", iteration + 1);

            // Оценка начальных позиций
            Map<String, Double> agentValues = new HashMap<>();
            for (Map.Entry<String, double[]> entry : agents.entrySet()) {
                double value = butaFunction(entry.getValue()[0], entry.getValue()[1]);
                System.out.printf("Для %s: начальная позиция (%.2f, %.2f), значение функции: %.4f%n", entry.getKey(), entry.getValue()[0], entry.getValue()[1], value);
                agentValues.put(entry.getKey(), value);
            }

            // Сортировка агентов по значениям функции
            List<Map.Entry<String, Double>> sortedAgents = new ArrayList<>(agentValues.entrySet());
            sortedAgents.sort(Comparator.comparingDouble(Map.Entry::getValue));

            String bestAgent = sortedAgents.get(0).getKey();
            String promisingAgent = sortedAgents.get(1).getKey();
            System.out.printf("Лидеры: Лучшая пчелка - %s, Перспективная пчелка - %s%n", bestAgent, promisingAgent);

            // Обновление позиций агентов на основе расстояния
            Set<String> checkedPairs = new HashSet<>();
            Map<String, double[]> newPositions = new HashMap<>();
            for (String agent1 : agents.keySet()) {
                for (String agent2 : agents.keySet()) {
                    if (!agent1.equals(agent2) && !checkedPairs.contains(agent1 + "-" + agent2)) {
                        double[] pos1 = agents.get(agent1);
                        double[] pos2 = agents.get(agent2);
                        double distance = euclideanDistance(pos1, pos2);
                        checkedPairs.add(agent1 + "-" + agent2);
                        checkedPairs.add(agent2 + "-" + agent1);

                        if (distance < threshold) {
                            double[] newPos = midpoint(pos1, pos2);
                            newPositions.put(agent1, newPos);
                            newPositions.put(agent2, newPos);
                        }
                    }
                }
            }

            for (Map.Entry<String, double[]> entry : newPositions.entrySet()) {
                agents.put(entry.getKey(), entry.getValue());
            }

            // Добавление дополнительных агентов вокруг лучшего и перспективного агентов
            List<double[]> additionalAgents = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                double x = Math.max(xMin, Math.min(xMax, agents.get(bestAgent)[0] + (rand.nextDouble() * 2 * delta - delta)));
                double y = Math.max(yMin, Math.min(yMax, agents.get(bestAgent)[1] + (rand.nextDouble() * 2 * delta - delta)));
                additionalAgents.add(new double[] { x, y });
            }
            for (int i = 0; i < M; i++) {
                double x = Math.max(xMin, Math.min(xMax, agents.get(promisingAgent)[0] + (rand.nextDouble() * 2 * delta - delta)));
                double y = Math.max(yMin, Math.min(yMax, agents.get(promisingAgent)[1] + (rand.nextDouble() * 2 * delta - delta)));
                additionalAgents.add(new double[] { x, y });
            }

            // Локальный поиск для всех агентов и дополнительных агентов
            List<String> newAgentKeys = new ArrayList<>();
            for (String agent : agents.keySet()) {
                double[] bestPosition = localSearch(agents.get(agent), delta);
                agents.put(agent, bestPosition);
                System.out.printf("Результат %s: новая позиция (%.2f, %.2f), оценка: %.4f%n", agent, bestPosition[0], bestPosition[1], butaFunction(bestPosition[0], bestPosition[1]));
            }
            for (int i = 0; i < additionalAgents.size(); i++) {
                double[] bestPosition = localSearch(additionalAgents.get(i), delta);
                System.out.printf("Результат %d: новая позиция (%.2f, %.2f), оценка: %.4f%n", i + 1, bestPosition[0], bestPosition[1], butaFunction(bestPosition[0], bestPosition[1]));
            }
        }

        // Финальные результаты
        for (Map.Entry<String, double[]> entry : agents.entrySet()) {
            double value = butaFunction(entry.getValue()[0], entry.getValue()[1]);
            System.out.printf("\n%s: Позиция (%.2f, %.2f), Функция: %.4f%n", entry.getKey(), entry.getValue()[0], entry.getValue()[1], value);
        }
    }
}
