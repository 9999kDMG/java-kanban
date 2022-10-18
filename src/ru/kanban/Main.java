package ru.kanban;

import ru.kanban.managers.HttpTaskManager;
import ru.kanban.managers.InMemoryTaskManager;
import ru.kanban.managers.Managers;
import ru.kanban.managers.TaskManager;
import ru.kanban.server.HttpTaskServer;
import ru.kanban.server.KVServer;
import ru.kanban.tasks.Epic;
import ru.kanban.tasks.Subtask;
import ru.kanban.tasks.Task;

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

        KVServer kvServer = new KVServer();
        kvServer.start();

        String nameFileToSave = "auto_save.txt";
        File fileToSave = createFile(nameFileToSave).toFile();
        TaskManager manager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);


        Task task1 = new Task("Почта", "Сходить на почту и получить посылку");
        task1.setStartTime(LocalDateTime.parse("12:00;04.10.22", DATE_TIME_FORMATTER));
        task1.setDuration(Duration.ofMinutes(5));

        Task task2 = new Task("Налоги", "Заплатить налоги и спать спокойно");
        task2.setStartTime(LocalDateTime.parse("10:00;04.10.22", DATE_TIME_FORMATTER));
        task2.setDuration(Duration.ofMinutes(15));

        Subtask subtask1 = new Subtask("Спиисок покупок", "Купить макароны", 1);
        Subtask subtask2 = new Subtask("Спиисок покупок", "Купить хлеб", 1);
        subtask1.setStartTime(LocalDateTime.parse("17:00;04.10.22", DATE_TIME_FORMATTER));
        subtask1.setDuration(Duration.ofMinutes(15));
        subtask2.setStartTime(LocalDateTime.parse("16:00;04.10.22", DATE_TIME_FORMATTER));
        subtask2.setDuration(Duration.ofMinutes(35));

        Epic epic1 = new Epic("Сходить в магазин", "Купить продукты");

        Epic epic2 = new Epic("Сходить в магазин", "Купить разное");

        Subtask subtask3 = new Subtask("Спиисок покупок", "Купить бекон", 2);
        Subtask subtask4 = new Subtask("Спиисок покупок", "Купить пельмени", 2);
        subtask3.setStartTime(LocalDateTime.parse("17:00;05.10.22", DATE_TIME_FORMATTER));
        subtask3.setDuration(Duration.ofMinutes(15));
        subtask4.setStartTime(LocalDateTime.parse("16:00;05.10.22", DATE_TIME_FORMATTER));
        subtask4.setDuration(Duration.ofMinutes(35));

        int task1Id = manager.createATask(task1);
        int task2Id = manager.createATask(task2);
        InMemoryTaskManager taskManager = (InMemoryTaskManager) manager;
        System.out.println(taskManager.getHistory());
        System.out.println();

        int epic1Id = manager.createAEpic(epic1);
        int subtask1Id = manager.createASubtask(subtask1);
        int subtask2Id = manager.createASubtask(subtask2);
        epic1.addSubtaskToEpic(subtask1Id);
        epic1.addSubtaskToEpic(subtask2Id);
        taskManager.checkDateTimeOfTheEpic(epic1Id);
        taskManager.checkStatusOfTheEpic(epic1Id);
        System.out.println(manager.getSubtaskById(subtask1Id));

        int epic2Id = manager.createAEpic(epic2);
        int subtask3Id = manager.createASubtask(subtask3);
        int subtask4Id = manager.createASubtask(subtask4);
        epic2.addSubtaskToEpic(subtask3Id);
        epic2.addSubtaskToEpic(subtask4Id);
        taskManager.checkDateTimeOfTheEpic(epic2Id);
        taskManager.checkStatusOfTheEpic(epic2Id);
        System.out.println(manager.getSubtaskById(subtask3Id));


        HttpTaskManager managerNew = (HttpTaskManager) Managers.getDefault();
        managerNew.load();
        System.out.println(managerNew.getListOfAllTasks());
        System.out.println(managerNew.getHistory());
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
