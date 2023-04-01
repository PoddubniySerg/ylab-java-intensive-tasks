package ru.ylab.practice.lesson05.eventsourcing.api;

import org.springframework.stereotype.Component;
import ru.ylab.practice.lesson05.eventsourcing.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataBaseClient {

    private final DataSource dataSource;

    public DataBaseClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Person findPerson(Long personId) throws SQLException {
        final String query = "select * from person where person_id=?;";
        final List<Person> persons = execute(query, personId);
        return persons.stream().findFirst().orElse(null);
    }

    public List<Person> findAll() throws SQLException {
        final String query = "select * from person;";
        return execute(query, null);
    }

    private List<Person> execute(String query, Long personId) throws SQLException {
        final List<Person> persons = new ArrayList<>();
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query)) {
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
}
