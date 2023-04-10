package io.ylab.intensive.lesson01;

import java.util.Random;
import java.util.Scanner;

public class Guess {

    public static void main(String[] args) {
        try (final Scanner scanner = new Scanner(System.in)) {
            int number = new Random().nextInt(99) + 1; // здесь загадывается число от 1 до 99
            int maxAttempts = 10; // здесь задается количество попыток
            System.out.println("Я загадал число. У тебя " + maxAttempts + " попыток угадать.");
            // ваш код здесь
            for (int i = 1; i <= maxAttempts; i++) {
                int input = scanner.nextInt();
                if (input > number) {
                    System.out.printf("Мое число меньше! У тебя осталось %d попыток\n", maxAttempts - i);
                } else if (input < number) {
                    System.out.printf("Мое число больше! У тебя осталось %d попыток\n", maxAttempts - i);
                } else {
                    System.out.printf("Ты угадал с %d попытки", i);
                    return;
                }
            }
            System.out.println("Ты нe угадал");
        }
    }
}
