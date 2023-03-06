package task1;

import java.util.Scanner;

public class Stars {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String template = scanner.next();
            String end = template + "\n";
            for (int i = 1; i <= n * m; i++) {
                System.out.print(i % m == 0 ? end : template);
            }
        }
    }
}