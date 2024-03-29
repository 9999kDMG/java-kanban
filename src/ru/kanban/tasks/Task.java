package ru.kanban.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected String name; //кратко описывает суть задачи
    protected String description; //детальное описание
    protected TaskStatus status; // статус выполнения

    protected LocalDateTime startTime;
    protected Duration duration;
    protected TypeTask type;
    protected int id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TypeTask.TASK;
    }

    public Task(int id, TypeTask type, String name, String description, TaskStatus status, LocalDateTime startTime, long duration) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(int id, TypeTask type, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime calculateEndTime() {
        return startTime.plus(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeTask getType() {
        return type;
    }

    public void setType(TypeTask type) {
        this.type = type;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status) &&
                (id == otherTask.id) &&
                Objects.equals(type, otherTask.type) &&
                Objects.equals(startTime, otherTask.startTime) &&
                Objects.equals(duration, otherTask.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, type, startTime, duration);
    }

    @Override
    public String toString() {
        return "ru.kanban.tasks.Task{" + "name=" + name + '\'' + "description=" + description + '\'' + "status=" + status.name() +
                '\'' + "localDT=" + startTime + '\'' + "duration=" + duration.toMinutes() + '\'' + "type=" + type +
                '\'' + "id=" + id + "}";
    }

    @Override
    public int compareTo(Task o) {
        return startTime.compareTo(o.startTime);
    }
}