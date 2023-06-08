import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Ініціалізація генератора випадкових чисел і отримання початкового стану
        Random rand = new Random();
        int[] initialState = getBoard(rand);

        // Виведення початкового стану дошки
        System.out.println("Початковий стан:");
        printBoard(initialState);

        // Використання імітації відпалу, щоб знайти рішення проблеми N-Queens
        int[] solution = simulatedAnnealing(initialState, 1000, 0.9995, 0.001);

        // Вивід повідомлення, чи знайдено рішення
        if (solution != null) {
            System.out.println("Найкращий стан:");
            printBoard(solution);
        } else {
            System.out.println("Рішення не знайдено.");
        }
    }

    // Цей метод використовує імітацію відпалу для пошуку рішення проблеми N-Queens
    static int[] simulatedAnnealing(int[] initialState, double initialTemperature, double coolingRate, double minimumTemperature) {
        // Присвоєння поточному стану копії початкового стану, а найкращому стану — поточного стану
        int[] currentState = initialState.clone();
        int[] bestState = currentState.clone();
        // Обчислення кількості конфліктів у поточному стані та встановлення найкращої енергії для цього значення
        int currentConflicts = calculateConflicts(currentState);
        int bestEnergy = currentConflicts;
        // Встановлення початкової температури
        double temperature = initialTemperature;

        // Виконання моделювання відпалу, поки температура не досягне мінімальної температури
        while (temperature > minimumTemperature) {
            // Створення наступного стану
            int[] nextState = generateNeighbor(currentState, new Random());
            // Підрахунок кількості конфліктів у наступному стані
            int nextConflicts = calculateConflicts(nextState);
            // Розрахунок зміни конфліктів між поточним станом і наступним станом
            int conflictsDelta = nextConflicts - currentConflicts;
            // Якщо наступний стан має менше конфліктів, ніж поточний стан,
            // або ймовірність прийняття сусіднього стану більша за випадкове число від 0 до 1,
            // відбувається перехід до наступного стану
            if (conflictsDelta <= 0 || Math.exp(-conflictsDelta / temperature) > new Random().nextDouble()) {
                currentState = nextState.clone();
                currentConflicts = nextConflicts;
                // Якщо кількість конфліктів у поточному стані менша за найкращу енергію,
                // знайдену на даний момент, відбувається оновлення найкращого стану і найкращої енергії
                if (currentConflicts < bestEnergy) {
                    bestState = currentState.clone();
                    bestEnergy = currentConflicts;
                }
            }
            // Зменшення температури
            temperature *= coolingRate;
        }
        // Повернення найкращого знайденого стану
        return bestState;
    }

    // Цей метод обчислює кількість конфліктів у заданому стані
    static int calculateConflicts(int[] state) {
        int conflicts = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = i + 1; j < state.length; j++) {
                // Перевірка наявності конфліктів у тому самому рядку, тому самому стовпці чи тій же діагоналі
                if (state[i] == state[j] || Math.abs(state[i] - state[j]) == j - i) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    // Цей метод генерує наступний стан шляхом випадкової зміни позиції одного ферзя
    static int[] generateNeighbor(int[] state, Random rand) {
        int[] neighbor = state.clone();
        int i = rand.nextInt(state.length);
        int j = rand.nextInt(state.length);
        neighbor[i] = j;
        return neighbor;
    }

    // Цей метод генерує початковий стан шахової дошки
    static int[] getBoard(Random rand) {
        int[] queens = new int[8]; //створення масиву, що відповідає розміру дошки
        for (int i = 0; i < queens.length; i++) {
            //заповнення масиву випадковими числами, що відповідають розміщенню ферзів по горизонталі на дошці
            queens[i] = rand.nextInt(8);
        }
        return queens;
    }

    // Цей метод виводить стан шахової дошки
    static void printBoard(int[] state) {
        int n = state.length;
        //якщо на поточній позиції в масиві state знаходиться ферзь, то виводиться "Q", інакше виводиться "."
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (state[i] == j) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}