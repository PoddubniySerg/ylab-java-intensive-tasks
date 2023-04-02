package io.ylab.intensive.lesson05.messagefilter.clients;

import io.ylab.intensive.lesson05.messagefilter.interfaces.LocaleStorage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class FilleReader implements LocaleStorage {

    private static final String FILE_PATH =
            "./src/main/java/io/ylab/intensive/lesson05/messagefilter/additions/filter_words.txt";

    @Override
    public List<String> getFilters() throws IOException {
        return Files.readAllLines(Path.of(FILE_PATH));
    }
}
