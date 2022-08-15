import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final List<Task> TaskHistory = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        TaskHistory.add(task);
        if (TaskHistory.size() > 10) {
            TaskHistory.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return TaskHistory;
    }

}
