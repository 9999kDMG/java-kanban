import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.kanban.tasks.Epic;
import ru.kanban.managers.FileBackedTasksManager;
import ru.kanban.managers.Managers;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest {

    @BeforeEach
    void loadManager() {
        this.manager = (FileBackedTasksManager) Managers.getDefault(fileToSave);
    }

    @Test
    void loadFromFile() throws IOException {
        Executable executable = new Executable() {
            @Override
            public void execute() {
                System.out.println("Hi");
            }
        };
        int epicId = manager.createAEpic(epic1);
        int subtaskId1 = manager.createASubtask(subtask1);
        int taskId = manager.createATask(task1);

        epic1.addSubtaskToEpic(subtaskId1);
        manager.checkDateTimeOfTheEpic(epicId);
        manager.checkStatusOfTheEpic(epicId);

        Epic epic = manager.getEpicById(epicId);
        epic.setId(epicId);
        subtask1.setId(subtaskId1);
        task1.setId(taskId);

        manager = manager.loadFromFile(fileToSave, fileToSave);
        assertEquals(epic, manager.getEpicById(epicId));
        assertEquals(List.of(task1), manager.getListOfAllTasks());
        assertEquals(List.of(subtask1), manager.getListOfAllSubtasks());
    }
}