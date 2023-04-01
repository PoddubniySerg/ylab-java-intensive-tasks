package io.ylab.intensive.lesson05.eventsourcing.api;

public class TableNotExistException extends Exception{

    public TableNotExistException() {
    }

    public TableNotExistException(String message) {
        super(message);
    }
}
