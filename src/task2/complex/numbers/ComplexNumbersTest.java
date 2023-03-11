package task2.complex.numbers;

import java.util.Scanner;

public class ComplexNumbersTest {

    public static void main(String[] args) {
        try (final Scanner scanner = new Scanner(System.in)) {
            final ComplexNumbersCalculator calculator = new ComplexNumbersCalculator();
            final ComplexNumber number1 = input("первого", scanner);
            final ComplexNumber number2 = input("второго", scanner);
            System.out.printf("Первое число: %s\n", number1);
            System.out.printf("Второе число: %s\n", number2);
            System.out.printf("Сложение: %s\n", calculator.sum(number1, number2));
            System.out.printf("Вычитание %s из %s: %s\n", number1, number2, calculator.sub(number1, number2));
            System.out.printf("Вычитание %s из %s: %s\n", number2, number1, calculator.sub(number2, number1));
            System.out.printf("Умножение: %s\n", calculator.multiplication(number1, number2));
            System.out.printf("Модуль числа %s: %s\n", number1, calculator.getModule(number1));
            System.out.printf("Модуль числа %s: %s\n", number2, calculator.getModule(number2));
        }
    }

    private static ComplexNumber input(String text, Scanner scanner) {
        System.out.printf("Введите действительную часть %s числа: ", text);
        final double real = scanner.nextDouble();
        System.out.printf("Введите мнимую часть %s числа: ", text);
        final double imaginary = scanner.nextDouble();
        return new ComplexNumber(real, imaginary);
    }
}
