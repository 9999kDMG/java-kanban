import java.util.Objects;

public class Subtask extends Task{
    private int numberOfEpic; //значение принадлежности к эпику

    Subtask(String name, String description) {
        super(name, description);
    }

    Subtask(String name, String description, int numberOfEpic) {
        super(name, description);
        this.numberOfEpic = numberOfEpic;
    }

    void setNumberOfEpic(int id) {
        this.numberOfEpic = id;
    }

    int getNumberOfEpic() {
        return this.numberOfEpic;
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
                Objects.equals(numberOfEpic, otherTask.numberOfEpic);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash += name.hashCode();
        }
        hash *= 31;
        if (description != null) {
            hash += description.hashCode();
        }
        hash *= 31;
        if (status != null) {
            hash += status.hashCode();
        }
        hash += numberOfEpic;
        return hash;
    }

    @Override
    public String toString() {
        return "Subtask{" + "name=" + name + '\'' +
                "description=" + description + '\'' +
                "status=" + status + '\'' +
                "numberOfEpic=" + numberOfEpic + "}";
    }
}