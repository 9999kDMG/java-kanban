import java.io.File;

public class Managers {
    public static TaskManager getDefault(File fileToSave) {
        return new FileBackedTasksManager(fileToSave);
    }

    public static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager<Task>();
    }
}
