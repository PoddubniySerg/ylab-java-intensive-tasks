package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Message;
import io.ylab.intensive.lesson04.eventsourcing.Actions;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ApiApp {


    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = initMQ();
        DataSource dataSource = initDB();
        // Тут пишем создание PersonApi, запуск и демонстрацию работы
        final Dao dao = new Dao(dataSource);
        final Messenger messenger = new Messenger(connectionFactory);
        final Mapper mapper = new Mapper();
        final PersonApi personApi = new PersonApiImpl(dao, messenger, mapper);
        personApi.savePerson(0L, "Kirill", "And", "Mifodiy");
        personApi.savePerson(1L, "Vasiliy", "Ivanov", null);
        personApi.savePerson(2L, "Petr", "Petrov", "Petrovich");
        personApi.savePerson(3L, "Aleksey", "Gagarin", "Alekseevich");
        personApi.savePerson(4L, "Irina", "Suchova", "Gennadievna");
        personApi.deletePerson(0L);
        personApi.deletePerson(0L);
        print(personApi.findPerson(0L), messenger, mapper);
        print(personApi.findPerson(3L), messenger, mapper);
        print(personApi.findAll(), messenger, mapper);
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }

    private static DataSource initDB() throws SQLException {
        return DbUtil.buildDataSource();
    }

    private static <T> void print(T object, Messenger messenger, Mapper mapper) {
        String s = null;
        if (object != null) {
            s = object.toString();
        }
        final Message message = new Message(Actions.PRINT.getAction(), s);
        final String json = mapper.getJson(message);
        messenger.sendJson(json);
    }
}
