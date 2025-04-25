/*package university;

import java.util.Random;

public class SimulatedAnnealing_2 {

    // Целевая функция, которую нужно минимизировать
    public static double function(double x, double y) {
        // Формула целевой функции
        return (x + 2 * y - 7) * (x + 2 * y - 7) + (2 * x + y - 5) * (2 * x + y - 5);
    }

    // Метод для генерации соседнего решения
    public static double[] generateNeighbor() {
        Random random = new Random();
        double newX = Math.round(random.nextDouble() * 20 - 10.0) / 100.0;
        double newY = Math.round(random.nextDouble() * 20 - 10.0) / 100.0;
        return new double[]{newX, newY};
    }

    // Реализация алгоритма имитации отжига
    public static double[] simulatedAnnealing(double T_initial, int iterations_per_temp, double T_min, double alpha) {
        // Инициализация начальной точки
        double x = 1.5; // начальная координата x
        double y = 1;  // начальная координата y
        double currentEnergy = function(x, y); // текущее значение функции
        double bestX = x, bestY = y; // лучшие найденные координаты
        double bestEnergy = currentEnergy; // лучшее значение функции

        double T = T_initial; // начальная температура
        int iteration = 1; // счетчик итераций

        // Вывод начального решения
        System.out.println("Начальное решение: x=" + x + ", y=" + y + ", значение функции f(x, y)=" + currentEnergy);

        // Основной цикл алгоритма
        while (T > T_min) { // Пока температура больше минимального значения
            System.out.printf("\nТемпература T(%d) = %.9f\n", iteration, T);

            // Выполняем заданное количество итераций при текущей температуре
            for (int i = 0; i < iterations_per_temp; i++) {
                // Генерация соседнего решения
                double[] neighbor = generateNeighbor();
                double newX = neighbor[0];
                double newY = neighbor[1];
                double neighborEnergy = function(newX, newY);
                double deltaE = neighborEnergy - currentEnergy; // Разница энергий

                // Вывод информации о текущем состоянии
                System.out.printf("Итерация %d: Текущие координаты x=%.2f, y=%.2f (f=%.2f)\n", iteration, x, y, currentEnergy);
                System.out.printf("            Соседние координаты x=%.2f, y=%.2f (f=%.2f)\n", newX, newY, neighborEnergy);
                System.out.printf("            ΔE = %.2f\n", deltaE);

                if (deltaE < 0) { // Если решение улучшилось
                    // Принять новое решение
                    x = newX;
                    y = newY;
                    currentEnergy = neighborEnergy;
                    System.out.println("            Решение улучшилось. Принято новое решение.");
                    if (neighborEnergy < bestEnergy) { // Обновление лучшего решения
                        bestX = newX;
                        bestY = newY;
                        bestEnergy = neighborEnergy;
                        System.out.printf("            Найдено новое лучшее решение: x=%.2f, y=%.2f с f(x, y)=%.2f\n", bestX, bestY, bestEnergy);
                    }
                } else { // Если решение ухудшилось
                    // Вероятностное принятие худшего решения
                    double probability = Math.exp(-deltaE / T);
                    double randomProb = Math.random();
                    System.out.printf("            Решение ухудшилось. Вероятность принятия: %.9f, Случайное число: %.4f\n", probability, randomProb);
                    if (randomProb < probability) { // Принять с некоторой вероятностью
                        x = newX;
                        y = newY;
                        currentEnergy = neighborEnergy;
                        System.out.println("            Решение принято по вероятности.");
                    } else {
                        System.out.println("            Решение отклонено.");
                    }
                }
            }

            // Уменьшение температуры
            iteration++;
            T *= alpha;
        }

        // Вывод лучшего найденного решения
        System.out.printf("\nАлгоритм завершён. Лучшее найденное решение: x=%.2f, y=%.2f с f(x, y)=%.2f\n", bestX, bestY, bestEnergy);
        return new double[]{bestX, bestY, bestEnergy};
    }

    public static void main(String[] args) {
        Random random = new Random(666); // Инициализация генератора случайных чисел для воспроизводимости

        // Параметры алгоритма
        double T_initial = 1000; // начальная температура
        int iterations_per_temp = 1; // количество итераций при каждой температуре
        double T_min = 0.0000001; // минимальная температура
        double alpha = 0.95; // коэффициент охлаждения

        // Запуск алгоритма
        double[] result = simulatedAnnealing(T_initial, iterations_per_temp, T_min, alpha);

        // Вывод результата
        System.out.printf("\nЛучшее найденное решение: x=%.2f, y=%.2f\n", result[0], result[1]);
        System.out.printf("Значение функции в лучшей точке: %.2f\n", result[2]);
    }
}*/

package university;

import java.util.Random;

public class SimulatedAnnealing_2 {

    // Функция, которую нужно минимизировать
    public static double function(double x, double y) {
        return Math.pow(x + 2 * y - 7, 2) + Math.pow(2 * x + y - 5, 2);
    }

    // Генерация нового случайного решения (координаты x и y в пределах [-10, 10])
    public static double[] generateNeighbor(double currentX, double currentY) {
        Random random = new Random();
        double deltaX = random.nextGaussian(); // Генерация небольшого изменения x
        double deltaY = random.nextGaussian(); // Генерация небольшого изменения y
        double newX = Math.min(10, Math.max(-10, currentX + deltaX)); // Ограничение в пределах [-10, 10]
        double newY = Math.min(10, Math.max(-10, currentY + deltaY));
        return new double[]{newX, newY};
    }


    public static void simulatedAnnealing(double initialTemperature, int iterationsPerTemp, double minTemperature) {
        Random random = new Random();

        // Начальные значения
        double x = 2;
        double y = -1;
        double currentEnergy = function(x, y);
        double bestX = x;
        double bestY = y;
        double bestEnergy = currentEnergy;
        double temperature = initialTemperature;

        System.out.printf("Начальное решение: x=%.2f, y=%.2f, значение функции f(x, y)=%.2f\n", x, y, currentEnergy);

        while (temperature > minTemperature) {
            System.out.printf("\nТемпература = %.9f\n", temperature);

            for (int i = 0; i < iterationsPerTemp; i++) {
                double[] neighbor = generateNeighbor(x, y);
                double newX = neighbor[0];
                double newY = neighbor[1];
                double neighborEnergy = function(newX, newY);
                double deltaE = neighborEnergy - currentEnergy;

                if (deltaE < 0) { // Новое решение лучше
                    x = newX;
                    y = newY;
                    currentEnergy = neighborEnergy;

                    if (neighborEnergy < bestEnergy) {
                        bestX = newX;
                        bestY = newY;
                        bestEnergy = neighborEnergy;
                    }
                } else {
                    double probability = Math.exp(-deltaE / temperature);
                    if (random.nextDouble() < probability) {
                        x = newX;
                        y = newY;
                        currentEnergy = neighborEnergy;
                    }
                }
            }

            // Экспоненциальное уменьшение температуры
            temperature *= 0.9;
        }

        System.out.printf("\nАлгоритм завершён. Лучшее найденное решение: x=%.2f, y=%.2f с f(x, y)=%.2f\n", bestX, bestY, bestEnergy);
    }

    public static void main(String[] args) {
        double initialTemperature = 100000; // Начальная температура
        int iterationsPerTemp = 100; // Количество итераций при каждой температуре
        double minTemperature = 0.000001; // Минимальная температура

        // Запуск алгоритма
        simulatedAnnealing(initialTemperature, iterationsPerTemp, minTemperature);
    }
}

