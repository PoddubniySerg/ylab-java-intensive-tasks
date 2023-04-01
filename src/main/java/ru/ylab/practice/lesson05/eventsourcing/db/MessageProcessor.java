package ru.ylab.practice.lesson05.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.ylab.practice.lesson05.eventsourcing.Actions;
import ru.ylab.practice.lesson05.eventsourcing.Message;
import ru.ylab.practice.lesson05.eventsourcing.Person;

@Component
public class MessageProcessor {

    private final DataBaseClient dbClient;
    private final MessageBrokerClient messageBrokerClient;
    private final ObjectMapper mapper;

    public MessageProcessor(DataBaseClient dbClient, MessageBrokerClient messageBrokerClient, ObjectMapper mapper) {
        this.dbClient = dbClient;
        this.messageBrokerClient = messageBrokerClient;
        this.mapper = mapper;
    }

    public void processSingleMessage() throws Exception {
        final String received = messageBrokerClient.getMessage();
        if (received != null) {
            final Message message = mapper.readValue(received, Message.class);
            execute(message);
        }
    }

    private void execute(Message message) throws Exception {
        try {
            if (message.getAction().equals(Actions.SAVE.getAction())) {
                final Person person = mapper.readValue(message.getContent(), Person.class);
                dbClient.save(person);
            } else if (message.getAction().equals(Actions.DELETE.getAction())) {
                final Long id = Long.parseLong(message.getContent());
                dbClient.delete(id);
            } else {
                System.out.println(message.getContent());
            }
        } catch (PersonNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
