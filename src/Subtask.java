import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int numberOfEpic; //значение принадлежности к эпику

    Subtask(String name, String description) {
        super(name, description);
    }

    Subtask(String name, String description, int numberOfEpic) {
        super(name, description);
        this.numberOfEpic = numberOfEpic;
        type = TypeTask.SUBTASK;
    }

    Subtask(int id, TypeTask type, String name, String description, TaskStatus status, int numberOfEpic, LocalDateTime startTime, long duration) {
        super(id, type, name, description, status, startTime, duration);
        this.numberOfEpic = numberOfEpic;
    }

    public void setNumberOfEpic(int id) {
        this.numberOfEpic = id;
    }

    public int getNumberOfEpic() {
        return numberOfEpic;
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
                Objects.equals(numberOfEpic, otherTask.numberOfEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, type, startTime, duration, numberOfEpic);
    }

    @Override
    public String toString() {
        return "Subtask{" + "name=" + name + '\'' +
                "description=" + description + '\'' +
                "status=" + status.name() + '\'' +
                "numberOfEpic=" + numberOfEpic +'\'' +
                "type=" + type.name() + "}";
    }
}