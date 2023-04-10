package io.ylab.intensive.lesson05.messagefilter.clients;

import io.ylab.intensive.lesson05.messagefilter.utils.DbNames;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Component
public class FilterDbClient {

    private static final String[] TABLE_TYPES = new String[]{"TABLE"};
    private static final boolean INSERT_BY_BATCH = true;
    private static final int BATCH_SIZE = 500_000;

    private final DataSource dataSource;

    public FilterDbClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(List<String> filters) throws SQLException {
        try (final Connection connection = dataSource.getConnection()) {
            if (tableNotExist(DbNames.TABLE.getName(), connection)) {
                createTable(DbNames.TABLE.getName(), DbNames.COLUMN.getName(), connection);
            }
            clearTable(DbNames.TABLE.getName(), connection);
            fillFilters(DbNames.TABLE.getName(), DbNames.COLUMN.getName(), filters, connection);
        }
    }

    private void fillFilters(String tableName, String columnName, List<String> filters, Connection connection) throws SQLException {
        final String ddl = String.format("insert into %s (%s) values (?);", DbNames.TABLE.getName(), DbNames.COLUMN.getName());
        final boolean insertByBatch = connection.getMetaData().supportsBatchUpdates() && INSERT_BY_BATCH;
        final int filtersSize = filters.size();
        try (final PreparedStatement statement = connection.prepareStatement(ddl)) {
            if (insertByBatch) {
                connection.setAutoCommit(false);
            }
            for (int i = 0; i < filtersSize; i++) {
                statement.setString(1, filters.get(i));
                if (insertByBatch) {
                    final int iteration = i + 1;
                    statement.addBatch();
                    if (iteration % BATCH_SIZE == 0 || iteration == filtersSize) {
                        statement.executeBatch();
                        connection.commit();
                    }
                } else {
                    statement.execute();
                }
            }
        }
    }

    private boolean tableNotExist(String tableName, Connection connection) throws SQLException {
        final DatabaseMetaData metaData = connection.getMetaData();
        try (final ResultSet resultSet =
                     metaData.getTables(null, null, tableName, TABLE_TYPES)) {
            return !resultSet.next();
        }
    }

    private void createTable(String tableName, String columnName, Connection connection) throws SQLException {
        final String ddl = String.format("create table %s (%s varchar primary key);", DbNames.TABLE.getName(), DbNames.COLUMN.getName());
        execute(ddl, connection);
    }

    private void clearTable(String tableName, Connection connection) throws SQLException {
        final String ddl = String.format("delete from %s;", DbNames.TABLE.getName());
        execute(ddl, connection);
    }

    private void execute(String ddl, Connection connection) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(ddl)) {
            statement.execute();
        }
    }
}
