package ru.ylab.practice.lesson03.file.sort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Sorter {

    private static final int PART_SIZE = 1_000_000;
    private static final int BUFFER_SIZE = 10_000_000;
    private static final String TEMP_FILE_NAME = "temp";
    private static final String TEMP_FILE_TYPE = ".txt";
    private static final String TEMP_DIRECTORY = "./src/task3/file/sort";

    public File sortFile(File dataFile) throws IOException {
        final int tempFilesCount = separateFile(dataFile);
        final Map<Long, BufferedReader> readers = getFileReaders(tempFilesCount);
        final File sortedFile = sortFiles(readers, dataFile.getParent());
        removeFiles(tempFilesCount);
        return sortedFile;
    }

    private int separateFile(File dataFile) throws IOException {
        try (final BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String data = reader.readLine();
            int fileIndex = 0;
            while (data != null) {
                final Path temp = getTempPath(fileIndex);
                final List<Long> values = new ArrayList<>();
                for (int i = 0; i < PART_SIZE && data != null; i++) {
                    values.add(Long.parseLong(data));
                    data = reader.readLine();
                }
                final String result = String.join("\n", values.stream().sorted().map(Object::toString).toList());
                Files.writeString(temp, result, StandardOpenOption.CREATE);
                fileIndex++;
            }
            return fileIndex;
        }
    }

    private Map<Long, BufferedReader> getFileReaders(int tempFilesCount) throws IOException {
        final Map<Long, BufferedReader> readers = new TreeMap<>();
        for (int i = 0; i < tempFilesCount; i++) {
            final BufferedReader reader = new BufferedReader(new FileReader(getTempPath(i).toFile()));
            final String startValue = reader.readLine();
            readers.put(Long.parseLong(startValue), reader);
        }
        return readers;
    }

    private File sortFiles(Map<Long, BufferedReader> readers, String directory) throws IOException {
        final File sortedFile = new File(directory, "new.txt");
        final Path path = sortedFile.toPath();
        Files.deleteIfExists(path);
        Files.createFile(path);
        while (!readers.isEmpty()) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < BUFFER_SIZE && !readers.isEmpty(); i++) {
                final Long min = readers.keySet().stream().findFirst().orElseThrow();
                final BufferedReader reader = readers.remove(min);
                stringBuilder.append(min).append("\n");
                final String next = reader.readLine();
                if (next != null) {
                    readers.put(Long.parseLong(next), reader);
                } else {
                    reader.close();
                }
            }
            Files.writeString(path, stringBuilder.toString(), StandardOpenOption.APPEND);
        }
        return sortedFile;
    }

    private void removeFiles(int tempFilesCount) throws IOException {
        for (int i = 0; i < tempFilesCount; i++) {
            Files.deleteIfExists(getTempPath(i));
        }
    }

    private Path getTempPath(int fileIndex) {
        return Path.of(TEMP_DIRECTORY, TEMP_FILE_NAME + fileIndex + TEMP_FILE_TYPE);
    }
}