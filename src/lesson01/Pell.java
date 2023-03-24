package lesson01;

import java.util.Scanner;

public class Pell {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            if (n == 0 || n == 1) {
                System.out.println(n);
                return;
            }
            long first = 0;
            long second = 1;
            for (int i = 2; i <= n; i++) {
                long pell = first + 2 * second;
                if (i == n) {
                    System.out.println(pell);
                    return;
                }
                first = second;
                second = pell;
            }
        }

    }
}
