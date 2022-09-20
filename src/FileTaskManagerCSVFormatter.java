import java.util.ArrayList;
import java.util.List;

public class FileTaskManagerCSVFormatter {

    public static String head() {
        return "id,type,name,description,status,epic";
    }

    public static String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(",")
                .append(task.getName())
                .append(',')
                .append(task.getDescription())
                .append(',')
                .append(task.getStatus().name());
        return sb.toString();
    }

    public static String subtaskToString(Subtask subtask) {
        StringBuilder sb = new StringBuilder();
        sb.append(taskToString(subtask))
                .append(',')
                .append(subtask.getNumberOfEpic());
        return sb.toString();
    }

    public static String epicToString(Epic epic) {
        StringBuilder sb = new StringBuilder();
        sb.append(taskToString(epic))
                .append(',')
                .append(epic.subtaskIdToString());
        return sb.toString();
    }

    public static String historyToString(HistoryManager<Task> historyManager) {
        List<Integer> list = historyManager.getHistoryId();
        StringBuilder sb = new StringBuilder();
        for (Integer id : list) {
            sb.append(id);
            sb.append(' ');
        }
        return sb.toString().strip();
    }

    public static Task tasksFromString(String line) {
        String[] dataOfLine = line.split(",");
        if (dataOfLine[1] == null || dataOfLine[4] == null) {
            try {
                throw new ReadDataException("Не удается правильно прочитать данные в файле");
            } catch (ReadDataException e) {
                System.out.println(e);
            }
        }
        Task taskFromString = null;
        for (int j = 0; j < dataOfLine.length; j++) {
            if (dataOfLine[1].equals(TypeTask.TASK.toString())) {
                Task task = new Task(dataOfLine[2], dataOfLine[3]);
                task.setStatus(statusFromString(dataOfLine[4]));
                task.setType(TypeTask.TASK);
                task.setId(Integer.parseInt(dataOfLine[0]));
                taskFromString = task;
            } else if (dataOfLine[1].equals(TypeTask.EPIC.toString())) {
                Epic epic = new Epic(dataOfLine[2], dataOfLine[3]);
                final int subtaskColumnNumbers = 5;
                if (dataOfLine.length > subtaskColumnNumbers) {
                    String[] idOfSubtask = dataOfLine[5].split(" ");
                    epic.setId(Integer.parseInt(dataOfLine[0]));
                    for (String string : idOfSubtask) {
                        int id = Integer.parseInt(string);
                        epic.addSubtaskToEpic(id);
                    }
                }
                epic.setType(TypeTask.EPIC);
                taskFromString = epic;
            } else if (dataOfLine[1].equals(TypeTask.SUBTASK.toString())) {
                Subtask subtask = new Subtask(dataOfLine[2], dataOfLine[3]);
                subtask.setStatus(statusFromString(dataOfLine[4]));
                int numberOfEpic = Integer.parseInt(dataOfLine[5]);
                subtask.setNumberOfEpic(numberOfEpic);
                subtask.setType(TypeTask.SUBTASK);
                subtask.setId(Integer.parseInt(dataOfLine[0]));
                taskFromString = subtask;
            }
        }
        return taskFromString;
    }

    private static TaskStatus statusFromString(String value) {
        if (value.equals(TaskStatus.NEW.toString())) {
            return TaskStatus.NEW;
        } else if (value.equals(TaskStatus.DONE.toString())) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    public static List<Integer> historyFromString(String value) throws ReadDataException {
        if (value.isEmpty()) {
            throw new ReadDataException("Отсутствует строка истории, значения не присвоены");
        }
        String[] historyOfTasks = value.split(" ");
        List<Integer> listId = new ArrayList<>();
        for (String string : historyOfTasks) {
            int id = Integer.parseInt(string);
            listId.add(id);
        }
        return listId;
    }
}
