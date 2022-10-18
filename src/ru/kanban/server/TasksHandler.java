package ru.kanban.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.kanban.managers.TaskManager;
import ru.kanban.server.Extradition;

import java.io.IOException;
import java.util.function.Consumer;

public class TasksHandler implements HttpHandler {
    private TaskManager manager;
    private final String PATH_TO_ALL = "/tasks";
    private final String PATH_TO_HISTORY = "/tasks/history";
    private final String PATH_TO_TASKS = "/tasks/task";
    private final String PATH_TO_SUBTASKS = "/tasks/subtask";
    private final String PATH_TO_EPICS = "/tasks/epic";

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        Extradition extradition = new Extradition(manager);

        switch (method) {
            case "GET":
                choiceRequest(exchange, PATH_TO_ALL, manager -> extradition.getListOfAll(exchange));
                choiceRequest(exchange, PATH_TO_TASKS, manager -> extradition.getTasks(exchange));
                choiceRequest(exchange, PATH_TO_SUBTASKS, manager -> extradition.getSubtasks(exchange));
                choiceRequest(exchange, PATH_TO_EPICS, manager -> extradition.getEpics(exchange));
                choiceRequest(exchange, PATH_TO_HISTORY, manager -> extradition.getHistory(exchange));
                break;
            case "POST":
                choiceRequest(exchange, PATH_TO_TASKS, manager -> extradition.postTasks(exchange));
                choiceRequest(exchange, PATH_TO_SUBTASKS, manager -> extradition.postTasks(exchange));
                choiceRequest(exchange, PATH_TO_EPICS, manager -> extradition.postTasks(exchange));
                break;
            case "DELETE":
                choiceRequest(exchange, PATH_TO_TASKS, manager -> extradition.deleteTasks(exchange));
                choiceRequest(exchange, PATH_TO_SUBTASKS, manager -> extradition.deleteTasks(exchange));
                choiceRequest(exchange, PATH_TO_EPICS, manager -> extradition.deleteTasks(exchange));
                choiceRequest(exchange, PATH_TO_ALL, manager -> extradition.deleteAll(exchange));
                break;
        }
    }

    private void choiceRequest(HttpExchange exchange, String patternPath, Consumer<HttpExchange> consumer) {
        String uri = exchange.getRequestURI().getPath();

        if (patternPath.equals(uri)) {
            consumer.accept(exchange);
        }
    }
}