package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {

    private static final String[] TABLE_TYPES = new String[]{"TABLE", "SYSTEM TABLE"};
    private static final String TABLE_NAME_KEY = "TABLE_NAME";
    private static final String COLUMN_NAME_KEY = "COLUMN_NAME";

    private final DataSource dataSource;

    public SQLQueryBuilderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String queryForTable(String tableName) throws SQLException {
        final List<String> tables = getTables();
        if (!tables.contains(tableName)) {
            return null;
        }
        final String columns = String.join(", ", getData(tableName, COLUMN_NAME_KEY));
        return String.format("SELECT %s FROM %s", columns, tableName);
    }

    @Override
    public List<String> getTables() throws SQLException {
        return getData(null, TABLE_NAME_KEY);
    }

    private List<String> getData(String tableName, String key) throws SQLException {
        try (final Connection connection = dataSource.getConnection();
             final ResultSet resultSet = getResultSet(tableName, connection)) {
            final List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString(key));
            }
            return result;
        }
    }

    private ResultSet getResultSet(String tableName, Connection connection) throws SQLException {
        final DatabaseMetaData metaData = connection.getMetaData();
        if (tableName == null) {
            return metaData.getTables(null, null, null, TABLE_TYPES);
        } else {
            return metaData.getColumns(null, null, tableName, null);
        }
    }
}
