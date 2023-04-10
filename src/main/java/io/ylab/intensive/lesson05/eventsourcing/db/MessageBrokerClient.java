package io.ylab.intensive.lesson05.eventsourcing.db;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class MessageBrokerClient {

    private static final String QUEUE_NAME = "queue";

    private final ConnectionFactory connectionFactory;

    public MessageBrokerClient(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String getMessage() throws IOException, TimeoutException {
        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            GetResponse response = channel.basicGet(QUEUE_NAME, true);
            if (response != null) {
                return new String(response.getBody());
            }
            return null;
        }
    }
}
