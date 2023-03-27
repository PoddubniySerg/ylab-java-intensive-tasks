package lesson04.eventsourcing.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lesson04.eventsourcing.Actions;
import lesson04.eventsourcing.Message;
import lesson04.eventsourcing.Person;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
    private static final String EXCHANGE_NAME = "exchange";
    private static final String QUEUE_NAME = "queue";
    private static final String KEY = "key";

    private final Dao dao;
    private final ConnectionFactory connectionFactory;
    private final ObjectMapper mapper;

    public PersonApiImpl(Dao dao, ConnectionFactory connectionFactory) {
        this.dao = dao;
        this.connectionFactory = connectionFactory;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void deletePerson(Long personId) {
        final Message message = new Message(Actions.DELETE.getAction(), personId.toString());
        sendMessage(message);
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        final Person person = new Person(personId, firstName, lastName, middleName);
        try {
            final String personJson = mapper.writeValueAsString(person);
            final Message message = new Message(Actions.SAVE.getAction(), personJson);
            sendMessage(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {
            final String json = mapper.writeValueAsString(message);
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, KEY);
            channel.basicPublish(EXCHANGE_NAME, KEY, null, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
