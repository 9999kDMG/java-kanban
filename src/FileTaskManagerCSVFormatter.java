import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileTaskManagerCSVFormatter {

    public static String head() {
        return "id,type,name,description,status,startTime,duration,epic";
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm;dd.MM.yy");

    private FileTaskManagerCSVFormatter() {

    }

    public static String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(",")
                .append(task.getName())
                .append(',')
                .append(task.getDescription())
                .append(',')
                .append(task.getStatus().name())
                .append(',')
                .append(task.getStartTime().format(DATE_TIME_FORMATTER))
                .append(',')
                .append(task.getDuration().toMinutes());
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
                .append(epic.getEndTime().format(DATE_TIME_FORMATTER))
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

    public static Task tasksFromString(String line) throws ReadDataException,
            ArrayIndexOutOfBoundsException, IllegalArgumentException {
        String[] dataOfLine = line.split(",");
        String id = dataOfLine[0];
        String type = dataOfLine[1];
        String name = dataOfLine[2];
        String description = dataOfLine[3];
        String status = dataOfLine[4];
        String startTime = dataOfLine[5];
        String duration = dataOfLine[6];
        if (type == null || status == null) {
            throw new ReadDataException("Не удается правильно интерпретировать данные в файле");
        }
        for (int j = 0; j < dataOfLine.length; j++) {
            if (TypeTask.TASK.toString().equals(type)) {
                return new Task(Integer.parseInt(id),
                        TypeTask.TASK, name, description,
                        TaskStatus.valueOf(status),
                        LocalDateTime.parse(startTime, DATE_TIME_FORMATTER), Integer.parseInt(duration));
            } else if (TypeTask.EPIC.toString().equals(type)) {
                Epic epic = new Epic(Integer.parseInt(id),
                        TypeTask.EPIC, name, description,
                        TaskStatus.valueOf(status),
                        LocalDateTime.parse(startTime, DATE_TIME_FORMATTER), Integer.parseInt(duration));
                epic.setEndTime(LocalDateTime.parse(dataOfLine[7], DATE_TIME_FORMATTER));
                final int SUBTASK_COLUMN_NUMBERS = 8;
                if (dataOfLine.length > SUBTASK_COLUMN_NUMBERS) {
                    String[] idOfSubtasks = dataOfLine[8].split(" ");
                    for (String string : idOfSubtasks) {
                        epic.addSubtaskToEpic(Integer.parseInt(string));
                    }
                }
                return epic;
            } else if (TypeTask.SUBTASK.toString().equals(type)) {
                int numberOfEpic = Integer.parseInt(dataOfLine[7]);
                return new Subtask(Integer.parseInt(id),
                        TypeTask.SUBTASK, name, description,
                        TaskStatus.valueOf(status), numberOfEpic,
                        LocalDateTime.parse(startTime, DATE_TIME_FORMATTER), Integer.parseInt(duration));
            }
        }
        return null;
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
