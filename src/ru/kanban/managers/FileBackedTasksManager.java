package ru.kanban.managers;

import ru.kanban.exceptions.ReadDataException;
import ru.kanban.exceptions.SaveDataException;
import ru.kanban.tasks.Epic;
import ru.kanban.tasks.Subtask;
import ru.kanban.tasks.Task;
import ru.kanban.tasks.TypeTask;
import ru.kanban.utils.FileTaskManagerCSVFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File fileToSave;

    public FileBackedTasksManager(File fileToSave) {
        this.fileToSave = fileToSave;
    }

    protected void save() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileToSave))) {
            br.write(FileTaskManagerCSVFormatter.head());
            br.newLine();
            for (Map.Entry<Integer, Task> entry : super.tasks.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(entry.getKey())
                        .append(",")
                        .append(TypeTask.TASK.name())
                        .append(FileTaskManagerCSVFormatter.taskToString(entry.getValue()));
                br.write(sb.toString());
                br.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : super.epics.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(entry.getKey())
                        .append(",")
                        .append(TypeTask.EPIC.name())
                        .append(FileTaskManagerCSVFormatter.epicToString(entry.getValue()));
                String finalString = sb.toString();
                br.write(finalString);
                br.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : super.subtasks.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(entry.getKey())
                        .append(",")
                        .append(TypeTask.SUBTASK.name())
                        .append(FileTaskManagerCSVFormatter.subtaskToString(entry.getValue()));
                String finalString = sb.toString();
                br.write(finalString);
                br.newLine();
            }
            br.newLine();
            br.write(FileTaskManagerCSVFormatter.historyToString(super.historyManager));
        } catch (IOException e) {
            throw new SaveDataException("Не удалось записать данные в файл");
        }
    }

    public FileBackedTasksManager loadFromFile(File fileToRead, File fileToWrite) throws IOException {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(fileToWrite);
        String csv = Files.readString(fileToRead.toPath());
        String[] lines = csv.split(System.lineSeparator());

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];

            if (line.isEmpty()) {
                HistoryManager<Task> HMFromFile = new InMemoryHistoryManager<>();
                try {
                    List<Integer> listID = FileTaskManagerCSVFormatter.historyFromString(lines[i + 1]);
                    for (Integer id : listID) {
                        HMFromFile.add(id, super.getTaskById(id));
                    }
                } catch (ReadDataException e) {
                    System.out.println(e);
                }
                super.historyManager = HMFromFile;
                break;
            }

            int idCounter = Integer.parseInt(line.substring(0, line.indexOf(',')));
            if (idCounter < super.id) {
                super.id = idCounter;
            }
            try {
                Task taskFromString = FileTaskManagerCSVFormatter.tasksFromString(line);
                tasksManager.addTask(taskFromString);
            } catch (ReadDataException | ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                System.out.println(e);
                throw new ReadDataException("Не удалось записать данные в файл");
            }
        }
        return tasksManager;
    }

    private void addTask(Task taskFromString) {
        if (taskFromString != null) {
            switch (taskFromString.getType()) {
                case TASK:
                    super.tasks.put(taskFromString.getId(), taskFromString);
                    break;
                case EPIC:
                    super.epics.put(taskFromString.getId(), (Epic) taskFromString);
                    break;
                case SUBTASK:
                    super.subtasks.put(taskFromString.getId(), (Subtask) taskFromString);
                    break;
            }
            save();
        }
    }

    @Override
    public int createATask(Task task) {
        int id = super.createATask(task);
        save();
        return id;
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public void clearAllTask() {
        super.clearAllTask();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void overwriteTask(int id, Task task) {
        super.overwriteTask(id, task);
        save();
    }

    @Override
    public List<Task> getListOfAllTasks() {
        return super.getListOfAllTasks();
    }

    @Override
    public int createASubtask(Subtask subtask) {
        int id = super.createASubtask(subtask);
        save();
        return id;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return super.getSubtaskById(id);
    }

    @Override
    public void clearAllSubtask() {
        super.clearAllSubtask();
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void overwriteSubtask(int id, Subtask subtask) {
        super.overwriteSubtask(id, subtask);
        save();
    }

    @Override
    public List<Subtask> getListOfAllSubtasks() {
        return super.getListOfAllSubtasks();
    }

    @Override
    public int createAEpic(Epic epic) {
        int id = super.createAEpic(epic);
        save();
        return id;
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public void clearAllEpic() {
        super.clearAllEpic();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void overwriteEpic(int id, Epic epic) {
        super.overwriteEpic(id, epic);
        save();
    }

    @Override
    public List<Epic> getListOfAllEpics() {
        return super.getListOfAllEpics();
    }

    @Override
    public List<Subtask> getListFromEpic(int id) {
        return super.getListFromEpic(id);
    }

    @Override
    public void checkStatusOfTheEpic(int id) {
        super.checkStatusOfTheEpic(id);
        save();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = super.getHistory();
        save();
        return history;
    }

    @Override
    public void checkDateTimeOfTheEpic(int id) {
        super.checkDateTimeOfTheEpic(id);
        save();
    }

    public HistoryManager<Task> getHistoryManager() {
        return super.historyManager;
    }
}
