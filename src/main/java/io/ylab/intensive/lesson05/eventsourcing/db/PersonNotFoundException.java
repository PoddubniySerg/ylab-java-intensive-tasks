package io.ylab.intensive.lesson05.eventsourcing.db;

public class PersonNotFoundException extends Exception {

    public PersonNotFoundException() {
        super();
    }

    public PersonNotFoundException(String message) {
        super(message);
    }
}
