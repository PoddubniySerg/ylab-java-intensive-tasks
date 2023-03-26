package lesson04.filesort;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSortImpl implements FileSorter {

    private static final String SORTED_FILE_NAME = "sorted.txt";
    private static final boolean SORT_BY_BATCH = true;
    private static final int BATCH_SIZE = 500_000;
    private static final int BUFFER_SIZE = 1_000_000;
    private static final String DELIMITER = "\n";
    private final DataSource dataSource;

    public FileSortImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public File sort(File data) {
        // ТУТ ПИШЕМ РЕАЛИЗАЦИЮ
        try {
            // сначала считаем количество и сумму строк из заданного файла для проверки результата
            checkCountValues(data);
            final int pages = readFile(data);
            final File sortedFile = new File(SORTED_FILE_NAME);
            initSortedFile(sortedFile);
            writeData(pages, sortedFile);
            return sortedFile;
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int readFile(File data) throws SQLException, IOException {
        try (final BufferedReader reader = new BufferedReader(new FileReader(data));
             final Connection connection = dataSource.getConnection()) {
            final boolean sortByBatch = connection.getMetaData().supportsBatchUpdates() && SORT_BY_BATCH;
            List<Long> values;
            int pages = 0;
            do {
                values = getValues(reader);
                insert(values, connection, sortByBatch);
                pages++;
            } while (!values.isEmpty());
            return pages;
        }
    }

    private void initSortedFile(File file) {
        try {
            Files.deleteIfExists(file.toPath());
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeData(int pages, File file) throws SQLException, IOException {
        try (final Connection connection = dataSource.getConnection()) {
            for (int i = 0; i < pages; i++) {
                final List<Long> values = getSortedData(i, connection);
                writeFile(file, values);
            }
            // считаем количество и сумму строк в конечном файле для сверки с начальным
            checkCountValues(file);
        }
    }

    private List<Long> getValues(BufferedReader reader) throws IOException {
        final List<Long> values = new ArrayList<>();
        for (int i = 0; i < BUFFER_SIZE; i++) {
            final String result = reader.readLine();
            if (result == null) {
                break;
            } else {
                values.add(Long.parseLong(result));
            }
        }
        return values;
    }

    private void insert(List<Long> values, Connection connection, boolean sortByBatch) throws SQLException {
        final int valuesSize = values.size();
        final String query = "insert into numbers (val) values (?)";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            if (sortByBatch) {
                connection.setAutoCommit(false);
            }
            for (int i = 0; i < valuesSize; i++) {
                statement.setLong(1, values.get(i));
                if (sortByBatch) {
                    executeBatch(statement, valuesSize, i + 1, connection);
                } else {
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            if (sortByBatch) {
                connection.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    private void executeBatch(PreparedStatement statement, int valuesSize, int iteration, Connection connection) throws SQLException {
        statement.addBatch();
        if (iteration % BATCH_SIZE == 0 || iteration == valuesSize) {
            statement.executeBatch();
            connection.commit();
        }
    }

    private List<Long> getSortedData(int page, Connection connection) throws SQLException {
        final String query = "select val from numbers order by val desc limit ? offset ?;";
        final List<Long> result = new ArrayList<>();
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, BUFFER_SIZE);
            statement.setInt(2, page * BUFFER_SIZE);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getLong(1));
            }
            return result;
        }
    }

    private void writeFile(File file, List<Long> values) throws IOException {
        final StringBuilder data = new StringBuilder();
        values.stream().map(Objects::toString).forEach(s -> data.append(s).append(DELIMITER));
        Files.writeString(file.toPath(), data, StandardOpenOption.APPEND);
    }

    private void checkCountValues(File file) {
        int counter = 0;
        long sum = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String result = reader.readLine();
            while (result != null) {
                sum += Long.parseLong(result);
                counter++;
                result = reader.readLine();
            }
            System.out.println("Count = " + counter);
            System.out.println("Sum = " + sum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
