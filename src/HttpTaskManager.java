import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).
            registerTypeAdapter(Duration.class, new GsonDurationAdapter()).
            create();

    private final KVTaskClient taskClient = new KVTaskClient();

    public HttpTaskManager() {
        super(null);
    }

    public void load() {
        try {
            Map<Integer, Task> tasks = gson.fromJson(
                    taskClient.load("tasks"),
                    new TypeToken<HashMap<Integer, Task>>() {
                    }.getType()
            );
            Map<Integer, Epic> epics = gson.fromJson(
                    taskClient.load("epics"),
                    new TypeToken<HashMap<Integer, Epic>>() {
                    }.getType()
            );
            Map<Integer, Subtask> subtasks = gson.fromJson(
                    taskClient.load("subtasks"),
                    new TypeToken<HashMap<Integer, Subtask>>() {
                    }.getType()
            );
            List<Task> historyList = gson.fromJson(
                    taskClient.load("history"),
                    new TypeToken<List<Task>>() {
                    }.getType()
            );
            InMemoryHistoryManager<Task> history = new InMemoryHistoryManager<>();
            historyList.forEach(history::add);

            int idLoad = Integer.parseInt(taskClient.load("id"));

            this.tasks = tasks;
            this.epics = epics;
            this.subtasks = subtasks;
            this.historyManager = history;
            this.sortedByTime.addAll(tasks.values());
            this.sortedByTime.addAll(epics.values());
            this.sortedByTime.addAll(subtasks.values());
            this.id = idLoad;
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при восстановлении данных");
        }
    }

    @Override
    protected void save() {
        try {
            taskClient.save("tasks", gson.toJson(tasks));
            taskClient.save("epics", gson.toJson(epics));
            taskClient.save("subtasks", gson.toJson(subtasks));
            taskClient.save("history", gson.toJson(historyManager.getHistory()));
            taskClient.save("id", gson.toJson(id));
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при сохранении данных");
        }
    }
}
