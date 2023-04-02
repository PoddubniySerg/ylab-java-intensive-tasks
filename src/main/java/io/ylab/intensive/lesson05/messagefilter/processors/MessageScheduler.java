package io.ylab.intensive.lesson05.messagefilter.processors;

import io.ylab.intensive.lesson05.messagefilter.processors.MessageProcessor;
import org.springframework.stereotype.Component;

@Component
public class MessageScheduler {

    private final MessageProcessor messageProcessor;

    public MessageScheduler(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public void start() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                messageProcessor.processSingleMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
