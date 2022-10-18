import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kanban.managers.FileBackedTasksManager;
import ru.kanban.managers.Managers;
import ru.kanban.tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

abstract class TaskManagerTest {
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm;dd.MM.yy");

    protected static final Task task1 = new Task("Почта", "получить посылку");

    protected static final Task task2 = new Task("Налоги", "Заплатить налоги");
    protected static final Subtask subtask1 = new Subtask("Купить", "макароны", 1);
    protected static final Subtask subtask2 = new Subtask("Купить", "хлеб", 1);
    protected static final Subtask subtask3 = new Subtask("Купить", "средство", 1);
    protected final Epic epic1 = new Epic("Сходить в магазин", "продукты");
    protected static final Epic epic2 = new Epic(0, TypeTask.EPIC, "Сходить в магазин", "продукты",
            TaskStatus.NEW, LocalDateTime.parse("10:00;04.10.22", DATE_TIME_FORMATTER), 0);

    protected final String file1 = "auto_save.txt";
    protected final File fileToSave = createFile(file1).toFile();

    protected FileBackedTasksManager manager;

    protected static Path createFile(String fileName) {

        Path filePath = Paths.get("test", fileName);
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                System.out.println("Ошибка при создании файла");
            }
        }
        return filePath;
    }

    //До тестов задать значения LocalDateTime для задач
    @BeforeAll
    static void setLocalDTTasks() {
        task1.setStartTime(LocalDateTime.parse("10:00;04.10.22", DATE_TIME_FORMATTER));
        task1.setDuration(Duration.ofMinutes(30));
        task2.setStartTime(LocalDateTime.parse("10:31;04.10.22", DATE_TIME_FORMATTER));
        task2.setDuration(Duration.ofMinutes(30));
        subtask1.setStartTime(LocalDateTime.parse("12:00;04.10.22", DATE_TIME_FORMATTER));
        subtask1.setDuration(Duration.ofMinutes(15));
        subtask2.setStartTime(LocalDateTime.parse("13:00;04.10.22", DATE_TIME_FORMATTER));
        subtask2.setDuration(Duration.ofMinutes(35));
        subtask3.setStartTime(LocalDateTime.parse("14:00;04.10.22", DATE_TIME_FORMATTER));
        subtask3.setDuration(Duration.ofMinutes(25));
        epic2.addSubtaskToEpic(0);
        epic2.setEndTime(LocalDateTime.parse("10:00;04.10.22", DATE_TIME_FORMATTER));

    }

    @BeforeEach
    void loadManager() {
        manager = (FileBackedTasksManager) Managers.getDefault(fileToSave);
    }

    //Нормальное поведение при создании задач
    @Test
    void shouldCreateATaskAndReturn1() {
        int taskId = manager.createATask(task1);
        assertEquals(1, taskId);
        assertEquals(task1, manager.getTaskById(taskId));
    }

    //Не должен создать задачу тк она null, вернет -1
    @Test
    void shouldDoNotCreateATaskAndReturnMinus() {
        assertNull(manager.getTaskById(1));
        assertEquals(-1, manager.createATask(null));
    }

    //Не должен создать задачу тк она пересекается по времени с предыдущей, вернет -1
    @Test
    void shouldDoNotCreateATaskBCIntersectionLDT() {
        task1.setDuration(Duration.ofMinutes(50));
        manager.createATask(task1);
        int taskId2 = manager.createATask(task2);
        assertNull(manager.getTaskById(taskId2));
    }

    //Должен создать две задачи
    @Test
    void shouldCreate2Tasks() {
        int taskId1 = manager.createATask(task1);
        int taskId2 = manager.createATask(task2);
        assertEquals(task1, manager.getTaskById(taskId1));
        assertEquals(task2, manager.getTaskById(taskId2));
        assertEquals(List.of(task1, task2), manager.getListOfAllTasks());
    }

    //Должен вернуть список задач
    @Test
    void shouldReturnListTasks() {
        manager.createATask(task1);
        manager.createATask(task2);
        assertEquals(List.of(task1, task2), manager.getListOfAllTasks());
    }

    //Должен вернуть задачу по ID
    @Test
    void shouldGetTaskById() {
        assertNull(manager.getTaskById(1)); //если задачи нет вернет null
        int taskId = manager.createATask(task1);
        assertEquals(task1, manager.getTaskById(taskId)); //если задача есть вернет ее
        assertNull(manager.getTaskById(2)); //если неправильный ID вернет null
    }

    @Test
    void shouldClearAllTask() {
        int taskId1 = manager.createATask(task1);
        int taskId2 = manager.createATask(task2);
        manager.clearAllTask();
        assertNull(manager.getTaskById(taskId1));
        assertNull(manager.getTaskById(taskId2));
    }

    @Test
    void shouldDeleteTask() {
        int taskId1 = manager.createATask(task1);
        manager.deleteTask(taskId1);
        assertNull(manager.getTaskById(taskId1));
        assertNull(manager.getTaskById(3));
    }

    @Test
    void shouldOverwriteTaskWhenHaveNotTaskInMemory() {
        int taskId1 = 1;
        manager.overwriteTask(1, task2);
        assertEquals(task2, manager.getTaskById(taskId1));
    }

    @Test
    void shouldOverwriteTaskWhenHaveTaskInMemory() {
        int taskId1 = manager.createATask(task1);
        manager.overwriteTask(taskId1, task2);
        assertEquals(task2, manager.getTaskById(taskId1));
    }

    //должен удалить эпик и все его сабтаски
    @Test
    void shouldDeleteEpic() {
        int epicId = manager.createAEpic(epic1);
        int subtaskId1 = manager.createASubtask(subtask1);
        int subtaskId2 = manager.createASubtask(subtask2);

        epic1.addSubtaskToEpic(subtaskId1);
        epic1.addSubtaskToEpic(subtaskId2);

        manager.deleteEpic(epicId);
        assertNull(manager.getEpicById(epicId));
        assertNull(manager.getSubtaskById(subtaskId1));
        assertNull(manager.getSubtaskById(subtaskId1));
    }

    @Test
    void getListFromEpic() {
        int epicId = manager.createAEpic(epic1);
        assertNull(manager.getListFromEpic(2)); //При неправильном ID вернет null

        assertEquals(List.of(), manager.getListFromEpic(epicId));//При отсутствии подзадач вернет пустой список

        int subtaskId1 = manager.createASubtask(subtask1);
        int subtaskId2 = manager.createASubtask(subtask2);
        int subtaskId3 = manager.createASubtask(subtask3);

        epic1.addSubtaskToEpic(subtaskId1);
        epic1.addSubtaskToEpic(subtaskId2);
        epic1.addSubtaskToEpic(subtaskId3);

        assertEquals(List.of(subtask1, subtask2, subtask3), manager.getListFromEpic(epicId)); //Нормальное поведение
    }

    //Статус эпика NEW при создании
    @Test
    void shouldEpicStatusNew() {
        int epicId = manager.createAEpic(epic1);
        assertEquals(TaskStatus.NEW, manager.getEpicById(epicId).getStatus());
    }

    //Статус эпика IN_PROGRESS, если хоть 1 сабтаск с этим статусом
    @Test
    void shouldEpicStatusInProgress() {
        int epicId = manager.createAEpic(epic1);
        int subtaskId1 = manager.createASubtask(subtask1);
        int subtaskId2 = manager.createASubtask(subtask2);
        int subtaskId3 = manager.createASubtask(subtask3);

        epic1.addSubtaskToEpic(subtaskId1);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);

        epic1.addSubtaskToEpic(subtaskId2);
        subtask2.setStatus(TaskStatus.NEW);

        epic1.addSubtaskToEpic(subtaskId3);
        subtask3.setStatus(TaskStatus.DONE);

        manager.checkStatusOfTheEpic(epicId);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicById(epicId).getStatus());
    }

    //Статус эпика DONE, если все сабтаски с этим статусом
    @Test
    void shouldEpicStatusDone() {
        int epicId = manager.createAEpic(epic1);
        int subtaskId1 = manager.createASubtask(subtask1);
        int subtaskId2 = manager.createASubtask(subtask2);

        epic1.addSubtaskToEpic(subtaskId1);
        subtask1.setStatus(TaskStatus.DONE);

        epic1.addSubtaskToEpic(subtaskId2);
        subtask2.setStatus(TaskStatus.DONE);

        manager.checkStatusOfTheEpic(epicId);
        assertEquals(TaskStatus.DONE, manager.getEpicById(epicId).getStatus());
    }

    //Проверка вычисления продолжительности эпика
    @Test
    void shouldEpicStartFromSubtask() {
        int epicId = manager.createAEpic(epic1);
        int subtaskId1 = manager.createASubtask(subtask1);
        int subtaskId2 = manager.createASubtask(subtask2);
        int subtaskId3 = manager.createASubtask(subtask3);

        epic1.addSubtaskToEpic(subtaskId1);
        epic1.addSubtaskToEpic(subtaskId2);
        epic1.addSubtaskToEpic(subtaskId3);

        manager.checkDateTimeOfTheEpic(epicId);
        assertEquals(LocalDateTime.parse("12:00;04.10.22", DATE_TIME_FORMATTER), epic1.getStartTime());
    }

    @Test
    void shouldEpicEndFromSubtask() {
        int epicId = manager.createAEpic(epic1);
        int subtaskId1 = manager.createASubtask(subtask1);
        int subtaskId2 = manager.createASubtask(subtask2);
        int subtaskId3 = manager.createASubtask(subtask3);

        epic1.addSubtaskToEpic(subtaskId1);
        epic1.addSubtaskToEpic(subtaskId2);
        epic1.addSubtaskToEpic(subtaskId3);

        manager.checkDateTimeOfTheEpic(epicId);
        assertEquals(LocalDateTime.parse("14:25;04.10.22", DATE_TIME_FORMATTER), epic1.getEndTime());
    }
}