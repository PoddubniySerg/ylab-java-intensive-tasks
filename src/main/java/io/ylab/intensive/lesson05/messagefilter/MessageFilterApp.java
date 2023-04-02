package io.ylab.intensive.lesson05.messagefilter;

import io.ylab.intensive.lesson05.messagefilter.processors.MessageScheduler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageFilterApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        final MessageScheduler scheduler = applicationContext.getBean(MessageScheduler.class);
        scheduler.start();
    }
}
