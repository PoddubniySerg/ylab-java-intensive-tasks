package ru.ylab.practice.lesson04.eventsourcing.api;

import ru.ylab.practice.lesson04.eventsourcing.Actions;
import ru.ylab.practice.lesson04.eventsourcing.Message;
import ru.ylab.practice.lesson04.eventsourcing.Person;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {

    private final Dao dao;
    private final Messenger messenger;
    private final Mapper mapper;

    public PersonApiImpl(Dao dao, Messenger messenger, Mapper mapper) {
        this.dao = dao;
        this.messenger = messenger;
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
            return dao.findPerson(personId);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Person> findAll() {
        try {
            return dao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void sendMessage(Message message) {
        final String json = mapper.getJson(message);
        if (json != null) {
            messenger.sendJson(json);
        }
    }
}
