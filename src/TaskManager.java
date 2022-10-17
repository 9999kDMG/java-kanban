import java.util.List;

public interface TaskManager {

    //методы обычных задач
    int createATask(Task task);

    Task getTaskById(int id);

    void clearAllTask();

    void deleteTask(int id);

    void overwriteTask(int id, Task task);

    List<Task> getListOfAllTasks();

    //методы подзадач
    int createASubtask(Subtask subtask);

    Subtask getSubtaskById(int id);

    void clearAllSubtask();

    void deleteSubtask(int id);

    void overwriteSubtask(int id, Subtask subtask);

    List<Subtask> getListOfAllSubtasks();

    //методы эпиков
    int createAEpic(Epic epic);

    Epic getEpicById(int id);

    void clearAllEpic();

    void deleteEpic(int id);

    void overwriteEpic(int id, Epic epic);

    List<Epic> getListOfAllEpics();

    List<Subtask> getListFromEpic(int id);

    List<Task> getHistory();
}
