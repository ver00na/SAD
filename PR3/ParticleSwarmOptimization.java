package university;

import java.util.Random;

public class ParticleSwarmOptimization {

    // Константы, определяющие параметры алгоритма
    private static final int POPULATION_SIZE = 1000; // Количество частиц в популяции
    private static final int DIMENSIONS = 2; // Размерность пространства поиска
    private static final int ITERATIONS = 500; // Максимальное количество итераций алгоритма
    private static final double INERTIA_WEIGHT = 0.5; // Вес инерционного компонента
    private static final double COGNITIVE_WEIGHT = 2.0; // Вес когнитивного компонента (личный опыт частицы)
    private static final double SOCIAL_WEIGHT = 2.0; // Вес социального компонента (влияние других частиц)
    private static final double LOWER_BOUND = -10.0; // Нижняя граница области поиска
    private static final double UPPER_BOUND = 10.0; // Верхняя граница области поиска

    public static void main(String[] args) {
        // Лучшее глобальное решение и его значение целевой функции
        double[] bestSolution = new double[DIMENSIONS];
        double bestFitness = Double.POSITIVE_INFINITY;

        // Инициализация популяции частиц и их скоростей
        double[][] population = initializePopulation(POPULATION_SIZE, DIMENSIONS);
        double[][] velocities = initializeVelocities(POPULATION_SIZE, DIMENSIONS);

        // Локальные лучшие значения для каждой частицы
        double[] localBestFitness = new double[POPULATION_SIZE];
        double[][] localBestPositions = new double[POPULATION_SIZE][DIMENSIONS];

        // Инициализация локальных лучших значений
        for (int i = 0; i < POPULATION_SIZE; i++) {
            localBestFitness[i] = Double.POSITIVE_INFINITY; // Первоначально задаем худшее значение
            localBestPositions[i][0] = population[i][0]; // Сохраняем текущую позицию
            localBestPositions[i][1] = population[i][1];
        }

        // Глобальное лучшее решение на текущий момент
        double[] globalBestPosition = new double[DIMENSIONS];

        // Печать начальных данных для отладки
        System.out.println("Начальные позиции частиц:");
        printArray(population);
        System.out.println("Начальные скорости частиц:");
        printArray(velocities);

        // Основной цикл алгоритма PSO
        for (int iteration = 0; iteration < ITERATIONS; iteration++) {
            for (int i = 0; i < POPULATION_SIZE; i++) {
                // Вычисление значения целевой функции для текущей позиции частицы
                double fitness = customFunction(population[i][0], population[i][1]);

                // Обновление локального лучшего значения для частицы
                if (fitness < localBestFitness[i]) {
                    localBestFitness[i] = fitness;
                    localBestPositions[i][0] = population[i][0];
                    localBestPositions[i][1] = population[i][1];
                }

                // Обновление глобального лучшего значения
                if (fitness < bestFitness) {
                    bestFitness = fitness;
                    globalBestPosition[0] = population[i][0];
                    globalBestPosition[1] = population[i][1];
                }

                // Обновление скорости и позиции частицы
                updateVelocity(velocities[i], population[i], INERTIA_WEIGHT, COGNITIVE_WEIGHT, SOCIAL_WEIGHT, globalBestPosition, localBestPositions[i]);
            }

            // Обновление позиций частиц с учетом новых скоростей
            for (int i = 0; i < POPULATION_SIZE; i++) {
                population[i][0] += velocities[i][0]; // Перемещение частицы по первой координате
                population[i][1] += velocities[i][1]; // Перемещение частицы по второй координате

                // Ограничение позиций частиц в пределах границ области поиска
                population[i][0] = Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, population[i][0]));
                population[i][1] = Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, population[i][1]));
            }
        }

        // Вывод результатов
        System.out.println("Лучшее решение для функции: [" + globalBestPosition[0] + " " + globalBestPosition[1] + "]");
        System.out.println("Значение функции: " + bestFitness);
    }

    // Определение целевой функции
    private static double customFunction(double x, double y) {
        return (x + 2 * y - 7) * (x + 2 * y - 7) + (2 * x + y - 5) * (2 * x + y - 5);
    }

    // Инициализация начальной популяции частиц
    private static double[][] initializePopulation(int populationSize, int dimensions) {
        Random random = new Random();
        double[][] population = new double[populationSize][dimensions];
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < dimensions; j++) {
                // Случайное значение в пределах области поиска
                population[i][j] = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * random.nextDouble();
            }
        }
        return population;
    }

    // Инициализация начальных скоростей частиц
    private static double[][] initializeVelocities(int populationSize, int dimensions) {
        Random random = new Random();
        double[][] velocities = new double[populationSize][dimensions];
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < dimensions; j++) {
                // Случайная скорость в пределах области поиска
                velocities[i][j] = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * random.nextDouble();
            }
        }
        return velocities;
    }

    // Обновление скорости частицы с учетом инерции, внутреннего и социального компонентов
    private static void updateVelocity(double[] velocity, double[] position, double inertiaWeight, double cognitiveWeight,
                                       double socialWeight, double[] globalBestPosition, double[] localBestPosition) {
        Random random = new Random();
        double r1 = random.nextDouble(); // Случайный коэффициент для внутреннего компонента
        double r2 = random.nextDouble(); // Случайный коэффициент для социального компонента

        for (int j = 0; j < position.length; j++) {
            // Формула обновления скорости
            velocity[j] = inertiaWeight * velocity[j] +
                    cognitiveWeight * r1 * (localBestPosition[j] - position[j]) +
                    socialWeight * r2 * (globalBestPosition[j] - position[j]);
        }
    }

    // Печать двумерного массива (позиций или скоростей частиц)
    private static void printArray(double[][] array) {
        for (double[] row : array) {
            System.out.print("[ ");
            for (double value : row) {
                System.out.printf("%.15f ", value); // Вывод значений с высокой точностью
            }
            System.out.println("]");
        }
    }
}
