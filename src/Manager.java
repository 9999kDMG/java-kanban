import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int idTask = 1; //идентификатор задачи
    private int idSubtask = 1; //идентификатор задачи
    private int idEpic = 1; //идентификатор задачи

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    //методы обычных задач
    int createATask(Task task) { //создание задачи
        if (task != null) {
            this.tasks.put(this.idTask, task);
            return this.idTask++;
        } else {
            return 0;
        }
    }

    Task getTaskById(int id) { //получение задачи по id
        return tasks.getOrDefault(id, null);
    }

    void clearAllTask() { //удалить все задачи
        this.tasks.clear();
        this.idTask = 1;
    }

    void deleteTask(int id) { //удалить задачу по id
        if (tasks.containsKey(id)) {
            this.tasks.remove(id);
            this.idTask -= 1;
        }
    }

    void overwriteTask(int id, Task task) { //перезаписать задачу
        if (task != null) {
            this.tasks.put(id, task);
        }
    }

    ArrayList<Task> getListOfAllTasks() { //получить список задач
        ArrayList<Task> listOfTasks = new ArrayList<>();
        listOfTasks.addAll(tasks.values());
        return listOfTasks;
    }

    //методы подзадач
    int createASubtask(Subtask subtask) { //создание подзадачи
        if (subtask != null) {
            this.subtasks.put(this.idSubtask, subtask);
            return this.idSubtask++;
        } else {
            return 0;
        }
    }

    Subtask getSubtaskById(int id) { //получение подзадачи по id
        return subtasks.getOrDefault(id, null);
    }

    void clearAllSubtask() { //удалить все подзадачи
        this.subtasks.clear();
        this.idSubtask = 1;
    }

    void deleteSubtask(int id) { //удалить подзадачу по id
        if (subtasks.containsKey(id)) {
            this.subtasks.remove(id);
            this.idSubtask -= 1;
        }
    }

    void overwriteSubtask(int id, Subtask subtask) { //перезаписать подзадачу
        if (subtask != null) {
            this.subtasks.put(id, subtask);
        }
    }

    ArrayList<Subtask> getListOfAllSubtasks() { //получить список подзадач
        ArrayList<Subtask> listOfSubtasks = new ArrayList<>();
        listOfSubtasks.addAll(subtasks.values());
        return listOfSubtasks;
    }

    //методы эпиков
    int createAEpic(Epic epic) { //создание эпика
        if (epic != null) {
            this.epics.put(this.idEpic, epic);
            return this.idEpic++;
        } else {
            return 0;
        }
    }

    Epic getEpicById(int id) { //получение эпика по id
        return epics.getOrDefault(id, null);
    }

    void clearAllEpic() { //удалить все эпики
        this.epics.clear();
        this.idEpic = 1;
    }

    void deleteEpic(int id) { //удалить эпик по id
        if (epics.containsKey(id)) {
            this.epics.remove(id);
            this.idEpic -= 1;
        }
    }

    void overwriteEpic(int id, Epic epic) { //перезаписать эпик
        if (epic != null) {
            this.epics.put(id, epic);
        }
    }

    ArrayList<Epic> getListOfAllEpics() { //получить список эпиков
        ArrayList<Epic> listOfEpics = new ArrayList<>();
        listOfEpics.addAll(epics.values());
        return listOfEpics;
    }

    ArrayList<Subtask> getListFromEpic1(int id) { //получить список подзадач эпиков
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Subtask o: subtasks.values()) {
            if (o.getNumberOfEpic() == id) {
                subtasksList.add(o);
            }
        }
        return subtasksList;
    }

    ArrayList<Subtask> getListFromEpic2(int id) { //получить список подзадач эпиков
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Integer o: epics.get(id).getContentOfTheEpic()) {
            subtasksList.add(subtasks.get(o));
        }
        return subtasksList;
    }

    void checkStatusOfTheEpic(int id){
        if (epics.containsKey(id)) {
            int numberOfTasks = getListFromEpic2(id).size();
            int statusNew = 0;
            int statusDone = 0;
            for (Subtask sTask: getListFromEpic2(id)) {
                if (sTask.getStatus().equals("NEW")) {
                    statusNew += 1;
                } else if (sTask.getStatus().equals("DONE")) {
                    statusDone += 1;
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