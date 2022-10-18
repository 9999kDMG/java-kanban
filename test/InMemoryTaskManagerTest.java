import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kanban.managers.FileBackedTasksManager;
import ru.kanban.managers.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void loadManager() {
        manager = (FileBackedTasksManager) Managers.getDefault(fileToSave);
    }

    @Test
    void ShouldGiveListPrioritizedTasks() {
        int taskId1 = manager.createATask(task1);
        int subtaskId1 = manager.createASubtask(subtask1);
        int subtaskId2 = manager.createASubtask(subtask2);
        int subtaskId3 = manager.createASubtask(subtask3);
        assertEquals(List.of(task1, subtask1, subtask2, subtask3), manager.getPrioritizedTasks());
    }
}