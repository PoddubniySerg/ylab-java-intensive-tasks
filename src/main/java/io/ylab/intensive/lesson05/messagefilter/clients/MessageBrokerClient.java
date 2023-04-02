package io.ylab.intensive.lesson05.messagefilter.clients;

import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class MessageBrokerClient {

    private static final String EXCHANGE_NAME = "exchange";
    private static final String INPUT_QUEUE_NAME = "input";
    private static final String OUTPUT_QUEUE_NAME = "output";
    private static final String OUTPUT_QUEUE_KEY = "output_key";

    private final ConnectionFactory connectionFactory;

    public MessageBrokerClient(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String getMessage() throws IOException, TimeoutException {
        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {
            channel.queueDeclare(INPUT_QUEUE_NAME, true, false, false, null);
            GetResponse response = channel.basicGet(INPUT_QUEUE_NAME, true);
            if (response != null) {
                return new String(response.getBody());
            }
            return null;
        }
    }

    public void sendMessage(String json) {
        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(OUTPUT_QUEUE_NAME, true, false, false, null);
            channel.queueBind(OUTPUT_QUEUE_NAME, EXCHANGE_NAME, OUTPUT_QUEUE_KEY);
            channel.basicPublish(EXCHANGE_NAME, OUTPUT_QUEUE_KEY, null, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
