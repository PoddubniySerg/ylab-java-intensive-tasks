package io.ylab.intensive.lesson03.file.sort;

import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        final long start = System.currentTimeMillis();
        File dataFile = new Generator().generate("data.txt", 370_000_000);
        System.out.println(new Validator(dataFile).isSorted()); // false
        File sortedFile = new Sorter().sortFile(dataFile);
        System.out.println(new Validator(sortedFile).isSorted()); // true
        System.out.println((System.currentTimeMillis() - start) / 1000);
    }
}
