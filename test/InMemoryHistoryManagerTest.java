import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kanban.tasks.Epic;
import ru.kanban.managers.InMemoryHistoryManager;
import ru.kanban.tasks.Subtask;
import ru.kanban.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    public InMemoryHistoryManager<Task> history;
    public static final Task task1 = new Task("Почта", "получить посылку");
    public static final Subtask subtask1 = new Subtask("Купить", "макароны", 1);
    public final Epic epic1 = new Epic("Сходить в магазин", "продукты");

    @BeforeEach
    void createHistoryManager() {
        history = new InMemoryHistoryManager<>();
    }

    @Test
    void shouldGetListHistory() {
        history.add(1, task1);
        history.add(2, subtask1);
        history.add(3, epic1);
        assertEquals(List.of(task1, subtask1, epic1), history.getHistory());
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        history.add(1, task1);
        history.add(2, subtask1);
        history.add(3, epic1);
        history.remove(2);
        assertEquals(List.of(task1, epic1), history.getHistory());
    }
}