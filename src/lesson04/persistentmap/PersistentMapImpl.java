package lesson04.persistentmap;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {

    private final DataSource dataSource;
    private String name;

    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init(String name) {
        this.name = name;
    }

    @Override
    public boolean containsKey(String key) throws SQLException {
        if (!isInitialized()) {
            return false;
        }
        final String getQuery = "select count(*) > 0 from persistent_map where map_name=? and key=?;";
        final List<String> params = List.of(this.name, key);
        boolean isContain = false;
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(getQuery)) {
            setParams(params, preparedStatement);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isContain = resultSet.getBoolean(1);
                resultSet.close();
            }
            return isContain;
        }
    }

    @Override
    public List<String> getKeys() throws SQLException {
        final List<String> keys = new ArrayList<>();
        if (isInitialized()) {
            final String getKeysQuery = "select key from persistent_map where map_name=?;";
            try (final Connection connection = dataSource.getConnection();
                 final PreparedStatement statement = connection.prepareStatement(getKeysQuery)) {
                statement.setString(1, this.name);
                final ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    keys.add(resultSet.getString(1));
                }
                resultSet.close();
            }
        }
        return keys;
    }

    @Override
    public String get(String key) throws SQLException {
        if (!isInitialized()) {
            return null;
        }
        final String getQuery = "select value from persistent_map where map_name=? and key=?;";
        final List<String> params = List.of(this.name, key);
        String result = null;
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(getQuery)) {
            setParams(params, preparedStatement);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString(1);
                resultSet.close();
            }
            return result;
        }
    }

    @Override
    public void remove(String key) throws SQLException {
        if (!isInitialized()) {
            return;
        }
        final String removeQuery = "delete from persistent_map where map_name=? and key=?;";
        final List<String> params = List.of(this.name, key);
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(removeQuery)) {
            setParams(params, preparedStatement);
            preparedStatement.execute();
        }
    }

    @Override
    public void put(String key, String value) throws SQLException {
        if (!isInitialized()) {
            return;
        }
        if (containsKey(key)) {
            remove(key);
        }
        final String insertQuery = "insert into persistent_map(map_name, key, value) values (?, ?, ?);";
        final List<String> params = List.of(this.name, key, value);
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            setParams(params, preparedStatement);
            preparedStatement.execute();
        }
    }

    @Override
    public void clear() throws SQLException {
        if (!isInitialized()) {
            return;
        }
        final String clearQuery = "delete from persistent_map where map_name=?;";
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(clearQuery)
        ) {
            preparedStatement.setString(1, this.name);
            preparedStatement.execute();
        }
    }

    private boolean isInitialized() {
        if (name == null) {
            System.out.println("Map is not initialized");
            return false;
        } else {
            return true;
        }
    }

    private void setParams(List<String> params, PreparedStatement statement) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            statement.setString(i + 1, params.get(i));
        }
    }
}
