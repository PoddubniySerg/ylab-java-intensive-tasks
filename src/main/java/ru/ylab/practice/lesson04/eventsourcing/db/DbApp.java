package ru.ylab.practice.lesson04.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import ru.ylab.practice.lesson04.DbUtil;
import ru.ylab.practice.lesson04.RabbitMQUtil;
import ru.ylab.practice.lesson04.eventsourcing.Actions;
import ru.ylab.practice.lesson04.eventsourcing.Message;
import ru.ylab.practice.lesson04.eventsourcing.Person;
import ru.ylab.practice.lesson04.eventsourcing.api.PersonNotFoundException;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DbApp {

    private static final String QUEUE_NAME = "queue";

    public static void main(String[] args) throws Exception {
        DataSource dataSource = initDb();
        ConnectionFactory connectionFactory = initMQ();

        // тут пишем создание и запуск приложения работы с БД
        final Dao dao = new Dao(dataSource);
        readMessages(connectionFactory, dao);
    }

    private static void readMessages(ConnectionFactory connectionFactory, Dao dao) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse response = channel.basicGet(QUEUE_NAME, true);
                if (response != null) {
                    final String received = new String(response.getBody());
                    final Message message = mapper.readValue(received, Message.class);
                    execute(message, dao, mapper);
                }
            }
        }
    }

    private static void execute(Message message, Dao dao, ObjectMapper mapper) throws Exception {
        try {
            if (message.getAction().equals(Actions.SAVE.getAction())) {
                final Person person = mapper.readValue(message.getContent(), Person.class);
                dao.save(person);
            } else if (message.getAction().equals(Actions.DELETE.getAction())){
                final Long id = Long.parseLong(message.getContent());
                dao.delete(id);
            } else {
                System.out.println(message.getContent());
            }
        } catch (PersonNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }

    private static DataSource initDb() throws SQLException {
        String ddl = ""
                + "drop table if exists person;"
                + "create table if not exists person (\n"
                + "person_id bigint primary key,\n"
                + "first_name varchar,\n"
                + "last_name varchar,\n"
                + "middle_name varchar\n"
                + ")";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(ddl, dataSource);
        return dataSource;
    }
}
