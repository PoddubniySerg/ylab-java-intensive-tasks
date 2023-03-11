package task1;

import java.util.Scanner;

public class Stars {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String template = scanner.next();
            for (int i = 1; i <= n * m; i++) {
                if (i % m == 0) {
                    System.out.println(template);
                } else {
                    System.out.print(template);
                }
            }
        }
    }
}