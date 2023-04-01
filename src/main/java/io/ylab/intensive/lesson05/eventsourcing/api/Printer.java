package io.ylab.intensive.lesson05.eventsourcing.api;

public interface Printer {

    <T> void print(T object);
}
