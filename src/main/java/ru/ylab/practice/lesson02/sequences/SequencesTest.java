package ru.ylab.practice.lesson02.sequences;

import java.util.Scanner;

public class SequencesTest {

    public static void main(String[] args) {
        try (final Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите число элементов n:");
            final int n = scanner.nextInt();
            final Sequences sequences = new SequencesImpl();
            sequences.a(n);
            sequences.b(n);
            sequences.c(n);
            sequences.d(n);
            sequences.e(n);
            sequences.f(n);
            sequences.g(n);
            sequences.h(n);
            sequences.i(n);
            sequences.j(n);
        }
    }
}
