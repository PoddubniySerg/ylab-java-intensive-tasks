package ru.ylab.practice.lesson02.snils.validator;

import java.util.Scanner;

public class SnilsValidatorTest {

    public static void main(String[] args) {
        try (final Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите снилс: ");
            final SnilsValidator validator = new SnilsValidatorImpl();
            System.out.println(validator.validate(scanner.nextLine()));
        }
    }
}
