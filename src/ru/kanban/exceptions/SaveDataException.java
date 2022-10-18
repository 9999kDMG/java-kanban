package ru.kanban.exceptions;

public class SaveDataException extends RuntimeException {
    public SaveDataException(String message) {
        super(message);
    }
}
