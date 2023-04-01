package io.ylab.intensive.lesson05.eventsourcing.db;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class MessageScheduler implements InitializingBean {

    private final MessageProcessor messageProcessor;

    public MessageScheduler(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        while (!Thread.currentThread().isInterrupted()) {
            messageProcessor.processSingleMessage();
        }
    }
}
