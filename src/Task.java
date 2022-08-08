import java.util.Objects;

public class Task {
    protected String name; //кратко описывает суть задачи
    protected String description; //детальное описание
    protected String status; // статус выполнения

    Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
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