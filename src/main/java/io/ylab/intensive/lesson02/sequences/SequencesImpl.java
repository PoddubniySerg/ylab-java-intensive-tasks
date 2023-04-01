package io.ylab.intensive.lesson02.sequences;

public class SequencesImpl implements Sequences {

    @Override
    public void a(int n) {
        System.out.print("A. ");
        for (int i = 0; i < n; i++) {
            final int item = (i + 1) * 2;
            printValue(i, n, item);
        }
    }

    @Override
    public void b(int n) {
        System.out.print("B. ");
        for (int i = 0; i < n; i++) {
            final int item = i * 2 + 1;
            printValue(i, n, item);
        }
    }

    @Override
    public void c(int n) {
        System.out.print("C. ");
        int pre = 0;
        for (int i = 0; i < n; i++) {
            final int item = pre + i * 2 + 1;
            pre = item;
            printValue(i, n, item);
        }
    }

    @Override
    public void d(int n) {
        System.out.print("D. ");
        for (double i = 0; i < n; i++) {
            final int item = (int) Math.pow(i + 1, 3d);
            printValue((int) i, n, item);
        }
    }

    @Override
    public void e(int n) {
        System.out.print("E. ");
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                printValue(i, n, 1);
            } else {
                printValue(i, n, -1);
            }
        }
    }

    @Override
    public void f(int n) {
        System.out.print("E. ");
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                printValue(i, n, i + 1);
            } else {
                printValue(i, n, -(i + 1));
            }
        }
    }

    @Override
    public void g(int n) {
        System.out.print("G. ");
        int pre = 0;
        for (int i = 0; i < n; i++) {
            final int item = pre + i * 2 + 1;
            pre = item;
            if (i % 2 == 0) {
                printValue(i, n, item);
            } else {
                printValue(i, n, -item);
            }
        }
    }

    @Override
    public void h(int n) {
        System.out.print("H. ");
        for (int i = 0; i < n; i++) {
            final int item = i / 2 + 1;
            if (i % 2 == 0) {
                printValue(i, n, item);
            } else {
                printValue(i, n, 0);
            }
        }
    }

    @Override
    public void i(int n) {
        System.out.print("G. ");
        int pre = 1;
        for (int i = 0; i < n; i++) {
            final int item = pre * (i + 1);
            pre = item;
            printValue(i, n, item);
        }
    }

    @Override
    public void j(int n) {
        System.out.print("J. ");
        int val1 = 0;
        int val2 = 1;
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                printValue(i, n, val2);
            } else {
                final int result = val1 + val2;
                val1 = val2;
                val2 = result;
                printValue(i, n, result);
            }
        }
    }

    private void printValue(int i, int n, int item) {
        if (i < n - 1) {
            System.out.printf("%d, ", item);
        } else {
            System.out.println(item + "...");
        }
    }
}
