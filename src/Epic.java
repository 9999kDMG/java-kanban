import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtaskIds;

    Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
    }

    void addSubtaskToEpic(Integer subtask) {
        subtaskIds.add(subtask);
    }

    ArrayList<Integer> getSubtaskIds() {
        return this.subtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" + "name=" + '\'' + name + '\'' +
                " description=" + '\'' + description + '\'' +
                " status=" + '\'' + status + '\'' +
                " subtaskIds=" + subtaskIds.toString() + "}";
    }
}