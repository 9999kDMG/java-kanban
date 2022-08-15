import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds;

    Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
    }

    public void addSubtaskToEpic(Integer subtask) {
        subtaskIds.add(subtask);
    }

    public List<Integer> getSubtaskIds() {
        return this.subtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" + "name=" + '\'' + name + '\'' +
                " description=" + '\'' + description + '\'' +
                " status=" + '\'' + status.name() + '\'' +
                " subtaskIds=" + subtaskIds.toString() + "}";
    }
}