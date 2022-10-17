import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends FileBackedTasksManagerTest {

    public KVServer kvServer;
    public HttpTaskServer httpTaskServer;
    public Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).
            registerTypeAdapter(Duration.class, new GsonDurationAdapter()).
            create();

    @BeforeEach
    public void beforeEach() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            TaskManager manager = Managers.getDefault();
            httpTaskServer = new HttpTaskServer(manager);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void shouldCreateTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest taskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .build();
        client.send(taskCreateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getTasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .GET()
                .build();

        List<Task> tasksResponse = gson.fromJson(
                client.send(getTasksRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Task>>() {
                }.getType());
        assertEquals(1, tasksResponse.size());
        assertEquals(1, tasksResponse.get(0).getId());
        assertEquals("TASK", tasksResponse.get(0).getType().name());
    }

    @Test
    public void shouldCreateSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest subtaskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1)))
                .build();
        client.send(subtaskCreateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getSubtasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .GET()
                .build();

        List<Subtask> subtasksResponse = gson.fromJson(
                client.send(getSubtasksRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Subtask>>() {
                }.getType());
        assertEquals(1, subtasksResponse.size());
        assertEquals(1, subtasksResponse.get(0).getId());
        assertEquals("SUBTASK", subtasksResponse.get(0).getType().name());
    }

    @Test
    public void shouldCreateEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest epicCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2)))
                .build();
        client.send(epicCreateRequest, HttpResponse.BodyHandlers.ofString()).body();

        HttpRequest getEpicsRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .GET()
                .build();

        List<Epic> epicsResponse = gson.fromJson(
                client.send(getEpicsRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Epic>>() {
                }.getType());
        assertEquals(1, epicsResponse.size());
        assertEquals(1, epicsResponse.get(0).getId());
        assertEquals("EPIC", epicsResponse.get(0).getType().name());
    }

    @Test
    public void shouldCreateAndDeleteAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest taskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .build();
        client.send(taskCreateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest epicCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2)))
                .build();
        client.send(epicCreateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest subtaskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask3)))
                .build();
        client.send(subtaskCreateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest deleteTaskRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .DELETE()
                .build();
        HttpRequest deleteEpic = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic?id=2"))
                .DELETE()
                .build();

        client.send(deleteTaskRequest, HttpResponse.BodyHandlers.ofString());
        client.send(deleteEpic, HttpResponse.BodyHandlers.ofString());

        HttpRequest getTasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .GET()
                .build();
        HttpRequest getEpicsRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .GET()
                .build();
        HttpRequest getSubtasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .GET()
                .build();

        List<Task> tasksResponse = gson.fromJson(
                client.send(getTasksRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Task>>() {
                }.getType()
        );

        List<Epic> epicsResponse = gson.fromJson(
                client.send(getEpicsRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Epic>>() {
                }.getType()
        );

        List<Epic> subtasksResponse = gson.fromJson(
                client.send(getSubtasksRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Epic>>() {
                }.getType()
        );

        assertEquals(0, tasksResponse.size());
        assertEquals(0, epicsResponse.size());
        assertEquals(1, subtasksResponse.size());
    }

    @Test
    public void shouldCorrectlyRestoreData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest taskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .build();
        client.send(taskCreateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest subtaskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2)))
                .build();
        client.send(subtaskCreateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest epicCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2)))
                .build();
        client.send(epicCreateRequest, HttpResponse.BodyHandlers.ofString());

        manager = new HttpTaskManager();
        ((HttpTaskManager) manager).load();

        List<Task> tasks = manager.getListOfAllTasks();
        List<Subtask> subtasks = manager.getListOfAllSubtasks();
        List<Epic> epics = manager.getListOfAllEpics();

        assertEquals(1, tasks.size());
        assertEquals(1, tasks.get(0).getId());
        assertEquals("TASK", tasks.get(0).getType().name());

        assertEquals(1, epics.size());
        assertEquals(3, epics.get(0).getId());
        assertEquals("EPIC", epics.get(0).getType().name());

        assertEquals(1, subtasks.size());
        assertEquals(2, subtasks.get(0).getId());
        assertEquals("SUBTASK", subtasks.get(0).getType().name());
    }
}
