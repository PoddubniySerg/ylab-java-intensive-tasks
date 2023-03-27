package lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import lesson04.DbUtil;
import lesson04.RabbitMQUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ApiApp {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = initMQ();
        DataSource dataSource = initDB();
        // Тут пишем создание PersonApi, запуск и демонстрацию работы
        final Dao dao = new Dao(dataSource);
        final PersonApi personApi = new PersonApiImpl(dao, connectionFactory);
        personApi.savePerson(0L, "Kirill", "And", "Mifodiy");
        personApi.savePerson(1L, "Vasiliy", "Ivanov", null);
        personApi.savePerson(2L, "Petr", "Petrov", "Petrovich");
        personApi.savePerson(3L, "Aleksey", "Gagarin", "Alekseevich");
        personApi.savePerson(4L, "Irina", "Suchova", "Gennadievna");
        personApi.deletePerson(0L);
        personApi.deletePerson(0L);
        System.out.println(personApi.findPerson(0L));
        System.out.println(personApi.findPerson(3L));
        System.out.println(personApi.findAll());
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }

    private static DataSource initDB() throws SQLException {
        return DbUtil.buildDataSource();
    }
}
