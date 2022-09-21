import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds;

    Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
    }

    Epic(int id, TypeTask type, String name, String description, TaskStatus status) {
        super(id, type, name, description, status);
        subtaskIds = new ArrayList<>();
    }

    public void addSubtaskToEpic(Integer subtask) {
        subtaskIds.add(subtask);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public String subtaskIdToString() {
        StringBuilder sb = new StringBuilder();
        for (Integer id : subtaskIds) {
            sb.append(id);
            sb.append(' ');
        }
        return sb.toString().strip();
    }

    @Override
    public String toString() {
        return "Epic{" + "name=" + '\'' + name + '\'' +
                " description=" + '\'' + description + '\'' +
                " status=" + '\'' + status.name() + '\'' +
                " subtaskIds=" + subtaskIds.toString() + "}";
    }
}