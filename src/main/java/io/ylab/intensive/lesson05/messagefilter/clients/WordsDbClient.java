package io.ylab.intensive.lesson05.messagefilter.clients;

import io.ylab.intensive.lesson05.messagefilter.utils.DbNames;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class WordsDbClient {

    private final DataSource dataSource;
    private final String ddl;

    public WordsDbClient(DataSource dataSource) {
        this.dataSource = dataSource;
        this.ddl =
                String.format("select count(*) from %s where %s ilike ?", DbNames.TABLE.getName(), DbNames.COLUMN.getName());
    }

    public boolean isForbidden(String word) throws SQLException {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(this.ddl)) {
            statement.setString(1, word);
            return execute(statement);
        }
    }

    private boolean execute(PreparedStatement statement) throws SQLException {
        try (final ResultSet resultSet = statement.executeQuery()) {
            int matchesCount = -1;
            if (resultSet.next()) {
                matchesCount = resultSet.getInt(1);
            }
            return matchesCount > 0;
        }
    }
}
