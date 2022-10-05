import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) throws IOException {

        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm;dd.MM.yy");

        String file1 = "auto_save.txt";
        File fileToSave = createFile(file1).toFile();
        TaskManager manager = Managers.getDefault(fileToSave);
        Task task1 = new Task("Почта", "Сходить на почту и получить посылку");
        task1.setStartTime(LocalDateTime.parse("12:00;04.10.22", DATE_TIME_FORMATTER));
        task1.setDuration(Duration.ofMinutes(25));

        Task task2 = new Task("Налоги", "Заплатить налоги и спать спокойно");
        task2.setStartTime(LocalDateTime.parse("10:00;04.10.22", DATE_TIME_FORMATTER));
        task2.setDuration(Duration.ofMinutes(25));

        Subtask subtask1 = new Subtask("Спиисок покупок", "Купить макароны", 1);
        Subtask subtask2 = new Subtask("Спиисок покупок", "Купить хлеб", 2);
        subtask1.setStartTime(LocalDateTime.parse("17:00;04.10.22", DATE_TIME_FORMATTER));
        subtask1.setDuration(Duration.ofMinutes(15));
        subtask2.setStartTime(LocalDateTime.parse("16:00;04.10.22", DATE_TIME_FORMATTER));
        subtask2.setDuration(Duration.ofMinutes(35));

        Epic epic1 = new Epic("Сходить в магазин", "Купить продукты");

        Epic epic2 = new Epic("Сходить в магазин", "Купить бытовую химию");

        Subtask subtask3 = new Subtask("Спиисок покупок", "Купить средство для мытья посуды");
        subtask3.setStartTime(LocalDateTime.parse("13:00;04.10.22", DATE_TIME_FORMATTER));
        subtask3.setDuration(Duration.ofMinutes(45));

        Subtask subtask4 = new Subtask("Спиисок покупок", "Купить средство для мытья посуды");
        subtask4.setStartTime(LocalDateTime.parse("18:05;04.10.22", DATE_TIME_FORMATTER));
        subtask4.setDuration(Duration.ofMinutes(5));

        int task1Id = manager.createATask(task1);
        int task2Id = manager.createATask(task2);
        System.out.println(manager.getTaskById(task1Id));
        System.out.println(manager.getTaskById(task2Id));
        System.out.println(manager.getTaskById(task2Id));
        InMemoryTaskManager taskManager = (InMemoryTaskManager) manager;
        System.out.println(taskManager.getHistory());
        System.out.println(manager.getTaskById(task1Id));
        System.out.println(taskManager.getHistory());
        System.out.println();
        System.out.println(taskManager.getHistory());
        System.out.println();

        int epic1Id = manager.createAEpic(epic1);
        subtask1.setNumberOfEpic(epic1Id);
        subtask2.setNumberOfEpic(epic1Id);
        subtask3.setNumberOfEpic(epic1Id);
        int subtask1Id = manager.createASubtask(subtask1);
        epic1.addSubtaskToEpic(subtask1Id);
        taskManager.checkDateTimeOfTheEpic(epic1Id);
        taskManager.checkStatusOfTheEpic(epic1Id);

        int subtask2Id = manager.createASubtask(subtask2);
        epic1.addSubtaskToEpic(subtask2Id);
        taskManager.checkDateTimeOfTheEpic(epic1Id);
        taskManager.checkStatusOfTheEpic(epic1Id);

        int subtask3Id = manager.createASubtask(subtask3);
        epic1.addSubtaskToEpic(subtask3Id);
        taskManager.checkDateTimeOfTheEpic(epic1Id);
        taskManager.checkStatusOfTheEpic(epic1Id);


        System.out.println(manager.getEpicById(epic1Id));
        System.out.println(manager.getSubtaskById(subtask1Id));
        System.out.println(manager.getSubtaskById(subtask2Id));
        System.out.println();
        System.out.println(taskManager.getHistory());
        System.out.println();

        int epic2Id = manager.createAEpic(epic2);
        subtask4.setNumberOfEpic(epic2Id);
        int subtask4Id = manager.createASubtask(subtask4);
        epic2.addSubtaskToEpic(subtask4Id);

        taskManager.checkDateTimeOfTheEpic(epic2Id);
        taskManager.checkStatusOfTheEpic(epic2Id);

        System.out.println(manager.getEpicById(epic2Id));
        System.out.println();
        System.out.println("PrioritizedTasks");
        System.out.println(taskManager.getPrioritizedTasks());

        String file2 = "auto_save2.txt";
        File fileToSave2 = createFile(file2).toFile();
        FileBackedTasksManager manager1 = new FileBackedTasksManager(fileToSave2);
        manager1 = manager1.loadFromFile(fileToSave, fileToSave2);
        System.out.println(manager1.getListOfAllTasks());
        System.out.println();
        System.out.println(manager1.getListOfAllSubtasks());
        System.out.println();
        System.out.println(manager1.getListOfAllEpics());
        System.out.println(manager1.getEpicById(epic1Id));
        System.out.println(manager1.getSubtaskById(subtask1Id));
        System.out.println(manager1.getSubtaskById(subtask2Id));
        manager1.getHistory();


    }

    public static Path createFile(String fileName) {

        Path filePath = Paths.get("src\\resources", fileName);
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                System.out.println("Ошибка при создании файла");
            }
        }
        return filePath;
    }
}
