import java.util.Objects;

public class Task {
    String name; //кратко описывает суть задачи
    String description; //детальное описание
    String status; // статус выполнения

    Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }

    void setStatus(String status) {
        this.status = status;
    }

    String getStatus() {
        return this.status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status);
    }

    @Override
    public String toString() {
        return "Task{" + "name=" + name + '\'' + "description=" + description + '\'' + "status=" + status + "}";
    }
}