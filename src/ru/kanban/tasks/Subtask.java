package ru.kanban.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId; //значение принадлежности к эпику

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        type = TypeTask.SUBTASK;
    }

    public Subtask(int id, TypeTask type, String name, String description, TaskStatus status, int epicId, LocalDateTime startTime, long duration) {
        super(id, type, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public void setEpicId(int id) {
        this.epicId = id;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Subtask otherTask = (Subtask) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status) &&
                (id == otherTask.id) &&
                Objects.equals(type, otherTask.type) &&
                Objects.equals(startTime, otherTask.startTime) &&
                Objects.equals(duration, otherTask.duration) &&
                Objects.equals(epicId, otherTask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, type, startTime, duration, epicId);
    }

    @Override
    public String toString() {
        return "ru.kanban.tasks.Subtask{" + "name=" + name + '\'' +
                "description=" + description + '\'' +
                "status=" + status.name() + '\'' +
                "epicId=" + epicId + '\'' +
                "type=" + type.name() + "}";
    }
}