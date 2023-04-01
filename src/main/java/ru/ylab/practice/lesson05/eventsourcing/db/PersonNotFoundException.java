package ru.ylab.practice.lesson05.eventsourcing.db;

public class PersonNotFoundException extends Exception {

    public PersonNotFoundException() {
        super();
    }

    public PersonNotFoundException(String message) {
        super(message);
    }
}
