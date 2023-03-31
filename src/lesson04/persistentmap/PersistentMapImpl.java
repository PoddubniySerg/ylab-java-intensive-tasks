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
        return getKeys().contains(key);
    }

    @Override
    public List<String> getKeys() throws SQLException {
        try {
            assertMapInit();
            final String getKeysQuery = "select key from persistent_map where map_name=?;";
            return getResult(getKeysQuery, this.name);
        } catch (MapNotInitException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public String get(String key) throws SQLException {
        try {
            assertMapInit();
            String result;
            final String getQuery = "select value from persistent_map where map_name=? and key=?;";
            final List<String> results = getResult(getQuery, this.name, key);
            if (!results.isEmpty()) {
                result = results.get(0);
            } else {
                result = "Значение не найдено";
            }
            return result;
        } catch (MapNotInitException e) {
            System.out.println(e.getMessage());
            return "Мапа не определена, выполните init()";
        }
    }

    @Override
    public void remove(String key) throws SQLException {
        try {
            assertMapInit();
            final String removeQuery = "delete from persistent_map where map_name=? and key=?;";
            executeQuery(removeQuery, this.name, key);
        } catch (MapNotInitException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void put(String key, String value) throws SQLException {
        try {
            assertMapInit();
            final String insertQuery = "insert into persistent_map(map_name, key, value) values (?, ?, ?);";
            if (containsKey(key)) {
                remove(key);
            }
            executeQuery(insertQuery, this.name, key, value);
        } catch (MapNotInitException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clear() throws SQLException {
        try {
            assertMapInit();
            final String clearQuery = "delete from persistent_map where map_name=?;";
            executeQuery(clearQuery, this.name);
        } catch (MapNotInitException e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeQuery(String query, String... params) throws SQLException {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            setParams(preparedStatement, params);
            preparedStatement.execute();
        }
    }

    private List<String> getResult(String query, String... params) throws SQLException {
        final List<String> results = new ArrayList<>();
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            setParams(preparedStatement, params);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                results.add(resultSet.getString(1));
            }
            resultSet.close();
            return results;
        }
    }

    private void assertMapInit() throws MapNotInitException {
        if (this.name == null) {
            throw new MapNotInitException("Map is not initialized");
        }
    }

    private void setParams(PreparedStatement statement, String... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
    }
}
