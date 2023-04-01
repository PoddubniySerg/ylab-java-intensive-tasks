package ru.ylab.practice.lesson05.eventsourcing.api;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class MessageBrokerClient {

    private static final String EXCHANGE_NAME = "exchange";
    private static final String QUEUE_NAME = "queue";
    private static final String KEY = "key";

    private final ConnectionFactory connectionFactory;

    public MessageBrokerClient(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void sendJson(String json) {
        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, KEY);
            channel.basicPublish(EXCHANGE_NAME, KEY, null, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
