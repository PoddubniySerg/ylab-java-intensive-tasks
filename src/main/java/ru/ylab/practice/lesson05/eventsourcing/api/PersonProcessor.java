package ru.ylab.practice.lesson05.eventsourcing.api;

import org.springframework.stereotype.Component;
import ru.ylab.practice.lesson05.eventsourcing.Actions;
import ru.ylab.practice.lesson05.eventsourcing.Message;
import ru.ylab.practice.lesson05.eventsourcing.Person;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonProcessor implements PersonApi, Printer {

    private final DataBaseClient dbClient;
    private final MessageBrokerClient messageBrokerClient;
    private final Mapper mapper;

    public PersonProcessor(DataBaseClient dbClient, MessageBrokerClient messageBrokerClient, Mapper mapper) {
        this.dbClient = dbClient;
        this.messageBrokerClient = messageBrokerClient;
        this.mapper = mapper;
    }

    @Override
    public void deletePerson(Long personId) {
        final Message message = new Message(Actions.DELETE.getAction(), personId.toString());
        sendMessage(message);
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        final Person person = new Person(personId, firstName, lastName, middleName);
        final String personJson = mapper.getJson(person);
        if (personJson != null) {
            final Message message = new Message(Actions.SAVE.getAction(), personJson);
            sendMessage(message);
        }
    }

    @Override
    public Person findPerson(Long personId) {
        try {
            return dbClient.findPerson(personId);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Person> findAll() {
        try {
            return dbClient.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public <T> void print(T object) {
        String s = null;
        if (object != null) {
            s = object.toString();
        }
        final ru.ylab.practice.lesson04.eventsourcing.Message message = new ru.ylab.practice.lesson04.eventsourcing.Message(ru.ylab.practice.lesson04.eventsourcing.Actions.PRINT.getAction(), s);
        final String json = mapper.getJson(message);
        messageBrokerClient.sendJson(json);
    }

    private void sendMessage(Message message) {
        final String json = mapper.getJson(message);
        if (json != null) {
            messageBrokerClient.sendJson(json);
        }
    }
}
