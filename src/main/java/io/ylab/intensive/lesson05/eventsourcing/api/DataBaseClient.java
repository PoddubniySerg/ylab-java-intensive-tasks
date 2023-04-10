package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataBaseClient {

    private static final String TABLE_NAME = "person";
    private static final String[] TABLE_TYPES = new String[]{"TABLE"};

    private final DataSource dataSource;

    public DataBaseClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Person findPerson(Long personId) throws SQLException, TableNotExistException {
        final String query = String.format("select * from %s where person_id=?;", TABLE_NAME);
        final List<Person> persons = execute(query, personId);
        return persons.stream().findFirst().orElse(null);
    }

    public List<Person> findAll() throws SQLException, TableNotExistException {
        final String query = String.format("select * from %s;", TABLE_NAME);
        return execute(query, null);
    }

    private List<Person> execute(String query, Long personId) throws SQLException, TableNotExistException {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query)) {
            if (tableNotExist(connection)) {
                throw new TableNotExistException("Таблица '" + TABLE_NAME + "' не найдена в базе данных");
            }
            final List<Person> persons = new ArrayList<>();
            if (personId != null) {
                statement.setLong(1, personId);
            }
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                final Person person = new Person();
                person.setId(resultSet.getLong(1));
                person.setName(resultSet.getString(2));
                person.setLastName(resultSet.getString(3));
                person.setMiddleName(resultSet.getString(4));
                persons.add(person);
            }
            return persons;
        }
    }

    private boolean tableNotExist(Connection connection) throws SQLException {
        final DatabaseMetaData metaData = connection.getMetaData();
        try (final ResultSet resultSet =
                     metaData.getTables(null, null, TABLE_NAME, TABLE_TYPES)) {
            return !resultSet.next();
        }
    }
}
