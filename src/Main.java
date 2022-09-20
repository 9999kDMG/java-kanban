import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {

        String file1 = "auto_save.txt";
        File fileToSave = createFile(file1).toFile();
        TaskManager manager = Managers.getDefault(fileToSave);
        Task task1 = new Task("Почта", "Сходить на почту и получить посылку");
        Task task2 = new Task("Налоги", "Заплатить налоги и спать спокойно");
        Subtask subtask1 = new Subtask("Спиисок покупок", "Купить макароны", 1);
        Subtask subtask2 = new Subtask("Спиисок покупок", "Купить хлеб", 2);
        Epic epic1 = new Epic("Сходить в магазин", "Купить продукты");
        Epic epic2 = new Epic("Сходить в магазин", "Купить бытовую химию");
        Subtask subtask3 = new Subtask("Спиисок покупок", "Купить средство для мытья посуды");

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
        int subtask2Id = manager.createASubtask(subtask2);
        epic1.addSubtaskToEpic(subtask2Id);
        int subtask3Id = manager.createASubtask(subtask3);
        epic1.addSubtaskToEpic(subtask3Id);
        System.out.println(manager.getEpicById(epic1Id));
        System.out.println(manager.getSubtaskById(subtask1Id));
        System.out.println(manager.getSubtaskById(subtask2Id));
        System.out.println();
        System.out.println(taskManager.getHistory());
        System.out.println();

        int epic2Id = manager.createAEpic(epic2);

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
