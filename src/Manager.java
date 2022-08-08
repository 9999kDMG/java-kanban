import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id = 0; //идентификатор задачи

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public int idCounter() {
        return id++;
    }

    //методы обычных задач
    public int createATask(Task task) { //создание задачи
        if (task != null) {
            tasks.put(id, task);
            return idCounter();
        } else {
            return -1;
        }
    }

    public Task getTaskById(int id) { //получение задачи по id
        return tasks.getOrDefault(id, null);
    }

    public void clearAllTask() { //удалить все задачи
        tasks.clear();
    }

    public void deleteTask(int id) { //удалить задачу по id
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void overwriteTask(int id, Task task) { //перезаписать задачу
        if (task != null) {
            tasks.put(id, task);
        }
    }

    public ArrayList<Task> getListOfAllTasks() { //получить список задач
        return new ArrayList<Task>(tasks.values());
    }

    //методы подзадач
    public int createASubtask(Subtask subtask) { //создание подзадачи
        if (subtask != null) {
            subtasks.put(id, subtask);
            return idCounter();
        } else {
            return -1;
        }
    }

    public Subtask getSubtaskById(int id) { //получение подзадачи по id
        return subtasks.getOrDefault(id, null);
    }

    public void clearAllSubtask() { //удалить все подзадачи
        subtasks.clear();
    }

    public void deleteSubtask(int id) { //удалить подзадачу по id
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        }
    }

    public void overwriteSubtask(int id, Subtask subtask) { //перезаписать подзадачу
        if (subtask != null) {
            subtasks.put(id, subtask);
        }
    }

    public ArrayList<Subtask> getListOfAllSubtasks() { //получить список подзадач
        return new ArrayList<Subtask>(subtasks.values());
    }

    //методы эпиков
    public int createAEpic(Epic epic) { //создание эпика
        if (epic != null) {
            epics.put(id, epic);
            return idCounter();
        } else {
            return -1;
        }
    }

    public Epic getEpicById(int id) { //получение эпика по id
        return epics.getOrDefault(id, null);
    }

    public void clearAllEpic() { //удалить все эпики
        epics.clear();
    }

    public void deleteEpic(int id) { //удалить эпик по id
        if (epics.containsKey(id)) {
            epics.remove(id);
        }
    }

    public void overwriteEpic(int id, Epic epic) { //перезаписать эпик
        if (epic != null) {
            epics.put(id, epic);
        }
    }

    public ArrayList<Epic> getListOfAllEpics() { //получить список эпиков
        return new ArrayList<Epic>(epics.values());
    }

    public ArrayList<Subtask> getListByIdEpic(int id) { //получить список подзадач эпиков
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Subtask o : subtasks.values()) {
            if (o.getNumberOfEpic() == id) {
                subtasksList.add(o);
            }
        }
        return subtasksList;
    }

    public ArrayList<Subtask> getListFromEpic(int id) { //получить список подзадач эпиков
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        if (epics.containsKey(id)) {
            for (Integer o : epics.get(id).getSubtaskIds()) {
                subtasksList.add(subtasks.get(o));
            }
        } else {
            return null;
        }
        return subtasksList;
    }

    public void checkStatusOfTheEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Subtask> listSubs = getListFromEpic(id);
            int numberOfTasks = listSubs.size();
            int statusNew = 0;
            int statusDone = 0;
            for (Subtask sTask : listSubs) {
                if (sTask.getStatus().equals("NEW")) {
                    statusNew++;
                } else if (sTask.getStatus().equals("DONE")) {
                    statusDone++;
                }
            }
            if (statusNew == numberOfTasks) {
                epics.get(id).setStatus("NEW");
            } else if (statusDone == numberOfTasks) {
                epics.get(id).setStatus("DONE");
            } else {
                epics.get(id).setStatus("IN_PROGRESS");
            }
        }
    }
}