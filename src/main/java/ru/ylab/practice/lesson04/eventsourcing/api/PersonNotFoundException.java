package ru.ylab.practice.lesson04.eventsourcing.api;

public class PersonNotFoundException extends Exception {

    public PersonNotFoundException() {
        super();
    }

    public PersonNotFoundException(String message) {
        super(message);
    }
}
