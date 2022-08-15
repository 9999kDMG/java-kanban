import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> taskHistory = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        taskHistory.add(task);
        final int MAX_SIZE_OF_THE_LIST = 10;
        if (taskHistory.size() > MAX_SIZE_OF_THE_LIST) {
            taskHistory.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }

}
