package ru.ylab.practice.lesson05.eventsourcing.api;

public interface Printer {

    <T> void print(T object);
}
