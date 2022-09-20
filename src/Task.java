import java.util.Objects;

public class Task {
    protected String name; //кратко описывает суть задачи
    protected String description; //детальное описание
    protected TaskStatus status; // статус выполнения

    protected TypeTask type;

    protected int id;

    Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
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
                Objects.equals(status, otherTask.status);
    }

    @Override
    public String toString() {
        return "Task{" + "name=" + name + '\'' + "description=" + description + '\'' + "status=" + status.name() + "}";
    }
}