package university;

import java.util.*;

public class SimulatedAnnealing {

    // Хранение расстояний между узлами в графе (ребра графа и их длины)
    private static final Map<String, Integer> edges = new HashMap<>();

    // Инициализация графа с весами рёбер
    static {
        edges.put("1,2", 20);
        edges.put("2,1", 20);
        edges.put("1,3", 40);
        edges.put("3,1", 30);
        edges.put("1,4", 32);
        edges.put("4,1", 32);
        edges.put("1,5", 10);
        edges.put("5,1", 10);
        edges.put("1,6", 22);
        edges.put("6,1", 22);
        edges.put("2,3", 30);
        edges.put("3,2", 30);
        edges.put("2,4", 21);
        edges.put("4,2", 21);
        edges.put("2,5", 17);
        edges.put("5,2", 17);
        edges.put("2,6", 10);
        edges.put("6,2", 10);
        edges.put("3,4", 31);
        edges.put("4,3", 31);
        edges.put("3,5", 45);
        edges.put("5,3", 45);
        edges.put("3,6", 32);
        edges.put("6,3", 32);
        edges.put("4,5", 23);
        edges.put("5,4", 23);
        edges.put("4,6", 18);
        edges.put("6,4", 18);
        edges.put("5,6", 27);
        edges.put("6,5", 27);
    }

    // Вычисляет общую длину маршрута на основе текущего порядка узлов
    public static double calculateTotalLength(List<Integer> route) {
        double total = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            String edge = route.get(i) + "," + route.get(i + 1);
            Integer length = edges.get(edge);
            if (length == null) {
                // Если маршрут не существует, возвращается бесконечность
                return Double.POSITIVE_INFINITY;
            }
            total += length;
        }
        return total;
    }

    // Генерирует соседнее решение (путем перестановки двух случайных узлов)
    public static List<Integer> generateNeighbor(List<Integer> route) {
        List<Integer> neighbor = new ArrayList<>(route);
        Random random = new Random();
        // Выбираем два случайных индекса (не включая начальный и конечный)
        int idx1 = random.nextInt(route.size() - 2) + 1;
        int idx2 = random.nextInt(route.size() - 2) + 1;
        while (idx1 == idx2) {
            idx2 = random.nextInt(route.size() - 2) + 1;
        }
        // Меняем местами выбранные узлы
        Collections.swap(neighbor, idx1, idx2);
        return neighbor;
    }

    // Алгоритм имитации отжига для оптимизации маршрута
    public static void simulatedAnnealing(List<Integer> initialRoute, double T_initial, double alpha, int iterationsPerTemp, double T_min) {
        List<Integer> currentRoute = new ArrayList<>(initialRoute); // Текущий маршрут
        double currentEnergy = calculateTotalLength(currentRoute); // Текущая длина маршрута
        List<Integer> bestRoute = new ArrayList<>(currentRoute); // Лучший найденный маршрут
        double bestEnergy = currentEnergy; // Длина лучшего маршрута

        double T = T_initial; // Начальная температура
        int totalIterations = 0; // Общее количество итераций

        System.out.println("Начальное решение: " + currentRoute + " с длиной " + currentEnergy);

        // Пока температура не станет меньше минимальной
        while (T > T_min) {
            System.out.printf("\nТемпература: %.9f\n", T);

            // Выполняем заданное количество итераций на каждой температуре
            for (int i = 0; i < iterationsPerTemp; i++) {
                totalIterations++;
                List<Integer> neighbor = generateNeighbor(currentRoute); // Генерация соседнего решения
                double neighborEnergy = calculateTotalLength(neighbor); // Вычисление длины соседнего маршрута
                double deltaE = neighborEnergy - currentEnergy; // Изменение длины маршрута

                System.out.println("Итерация " + totalIterations + ": Текущее решение " + currentRoute + " (E=" + currentEnergy + ")");
                System.out.println("            Соседнее решение " + neighbor + " (E=" + neighborEnergy + ")");
                System.out.println("            ΔE = " + deltaE);

                if (deltaE < 0) {
                    // Если длина маршрута уменьшилась, принимаем новое решение
                    currentRoute = neighbor;
                    currentEnergy = neighborEnergy;
                    System.out.println("            Решение улучшилось. Принято новое решение.");
                    if (neighborEnergy < bestEnergy) {
                        // Обновляем лучшее найденное решение
                        bestRoute = neighbor;
                        bestEnergy = neighborEnergy;
                        System.out.println("            Найдено новое лучшее решение: " + bestRoute + " с длиной " + bestEnergy);
                    }
                } else {
                    // Если решение ухудшилось, принимаем его с некоторой вероятностью
                    double probability = Math.exp(-deltaE / T);
                    double randomProb = Math.random();
                    System.out.printf("            Решение ухудшилось. Вероятность принятия: %.9f, Случайное число: %.4f\n", probability, randomProb);
                    if (randomProb < probability) {
                        currentRoute = neighbor;
                        currentEnergy = neighborEnergy;
                        System.out.println("            Решение принято по вероятности.");
                    } else {
                        System.out.println("            Решение отклонено.");
                    }
                }
            }
            // Уменьшение температуры
            T *= alpha;
        }
        // Вывод лучшего решения
        System.out.println("\nАлгоритм завершён. Лучшее найденное решение: " + bestRoute + " с длиной " + bestEnergy);
    }

    public static void main(String[] args) {
        // Начальный маршрут
        List<Integer> initialRoute = Arrays.asList(1, 3, 4, 5, 6, 2, 1);

        // Параметры алгоритма
        double T_initial = 100; // Начальная температура
        double alpha = 0.5; // Коэффициент охлаждения
        int iterationsPerTemp = 1; // Количество итераций на каждой температуре
        double T_min = 0.0000001; // Минимальная температура

        // Запуск алгоритма
        simulatedAnnealing(initialRoute, T_initial, alpha, iterationsPerTemp, T_min);
    }
}
