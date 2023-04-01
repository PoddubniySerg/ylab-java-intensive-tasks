package ru.ylab.practice.lesson05.eventsourcing.db;

import org.springframework.stereotype.Component;
import ru.ylab.practice.lesson05.eventsourcing.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DataBaseClient {

    private final DataSource dataSource;

    public DataBaseClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Person person) throws SQLException {
        final String query = "" +
                "insert into person (person_id, first_name, last_name, middle_name) " +
                "values (?, ?, ?, ?) " +
                "on conflict (person_id) do update " +
                "set " +
                "first_name=excluded.first_name, " +
                "last_name=excluded.last_name, " +
                "middle_name=excluded.middle_name;";
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, person.getId());
            statement.setString(2, person.getName());
            statement.setString(3, person.getLastName());
            statement.setString(4, person.getMiddleName());
            statement.execute();
        }
    }

    public void delete(Long personId) throws PersonNotFoundException, SQLException {
        final String query = "delete from person where person_id=? returning person_id;";
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, personId);
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new PersonNotFoundException("Неудачная попытка удалить Person с идентификатором " + personId);
            }
        }
    }
}
