import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int id = 0; //идентификатор задачи

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    private final HistoryManager<Task> historyManager = new InMemoryHistoryManager<>();

    @Override
    public int idCounter() {
        return id++;
    }

    //методы обычных задач
    @Override
    public int createATask(Task task) { //создание задачи
        if (task != null) {
            tasks.put(id, task);
            return idCounter();
        } else {
            return -1;
        }
    }

    @Override
    public Task getTaskById(int id) { //получение задачи по id
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            historyManager.add(id, task);
            return task;
        } else {
            return null;
        }
    }

    @Override
    public void clearAllTask() { //удалить все задачи
        tasks.clear();
    }

    @Override
    public void deleteTask(int id) { //удалить задачу по id
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void overwriteTask(int id, Task task) { //перезаписать задачу
        if (task != null) {
            tasks.put(id, task);
        }
    }

    @Override
    public List<Task> getListOfAllTasks() { //получить список задач
        return new ArrayList<Task>(tasks.values());
    }

    //методы подзадач
    @Override
    public int createASubtask(Subtask subtask) { //создание подзадачи
        if (subtask != null) {
            subtasks.put(id, subtask);
            return idCounter();
        } else {
            return -1;
        }
    }

    @Override
    public Subtask getSubtaskById(int id) { //получение подзадачи по id
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            historyManager.add(id, subtask);
            return subtask;
        } else {
            return null;
        }
    }

    @Override
    public void clearAllSubtask() { //удалить все подзадачи
        subtasks.clear();
    }

    @Override
    public void deleteSubtask(int id) { //удалить подзадачу по id
        historyManager.remove(id);
        subtasks.remove(id);
    }

    @Override
    public void overwriteSubtask(int id, Subtask subtask) { //перезаписать подзадачу
        if (subtask != null) {
            subtasks.put(id, subtask);
        }
    }

    @Override
    public List<Subtask> getListOfAllSubtasks() { //получить список подзадач
        return new ArrayList<Subtask>(subtasks.values());
    }

    //методы эпиков
    @Override
    public int createAEpic(Epic epic) { //создание эпика
        if (epic != null) {
            epics.put(id, epic);
            return idCounter();
        } else {
            return -1;
        }
    }

    @Override
    public Epic getEpicById(int id) { //получение эпика по id
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            historyManager.add(id, epic);
            return epic;
        } else {
            return null;
        }
    }

    @Override
    public void clearAllEpic() { //удалить все эпики
        epics.clear();
    }

    @Override
    public void deleteEpic(int id) { //удалить эпик по id
        Epic epic = getEpicById(id);
        List<Integer> subtaskIds = epic.getSubtaskIds();
        for (Integer o : subtaskIds) {
            deleteSubtask(o);
        }
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void overwriteEpic(int id, Epic epic) { //перезаписать эпик
        if (epic != null) {
            epics.put(id, epic);
        }
    }

    @Override
    public List<Epic> getListOfAllEpics() { //получить список эпиков
        return new ArrayList<Epic>(epics.values());
    }

    //получить список подзадач эпика, через ID, хранящегося внутри подзадачи
    @Override
    public List<Subtask> getListByIdEpic(int id) {
        List<Subtask> subtasksList = new ArrayList<>();
        for (Subtask o : subtasks.values()) {
            if (o.getNumberOfEpic() == id) {
                subtasksList.add(o);
            }
        }
        return subtasksList;
    }

    //получить список подзадач эпика, через ID, хранящегося внутри эпика
    @Override
    public List<Subtask> getListFromEpic(int id) {
        List<Subtask> subtasksList = new ArrayList<>();
        if (epics.containsKey(id)) {
            for (Integer o : epics.get(id).getSubtaskIds()) {
                subtasksList.add(subtasks.get(o));
            }
        } else {
            return null;
        }
        return subtasksList;
    }

    @Override
    public void checkStatusOfTheEpic(int id) {
        if (epics.containsKey(id)) {
            List<Subtask> listSubs = getListFromEpic(id);
            int numberOfTasks = listSubs.size();
            int statusNew = 0;
            int statusDone = 0;
            for (Subtask sTask : listSubs) {
                if (sTask.getStatus() == TaskStatus.NEW) {
                    statusNew++;
                } else if (sTask.getStatus() == TaskStatus.DONE) {
                    statusDone++;
                }
            }
            if (statusNew == numberOfTasks) {
                epics.get(id).setStatus(TaskStatus.NEW);
            } else if (statusDone == numberOfTasks) {
                epics.get(id).setStatus(TaskStatus.DONE);
            } else {
                epics.get(id).setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
