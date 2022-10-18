package ru.kanban.exceptions;

public class ReadDataException extends RuntimeException {
    public ReadDataException(final String message) {
        super(message);
    }
}
