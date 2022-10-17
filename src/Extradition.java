import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Extradition {
    private TaskManager manager;
    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).
            registerTypeAdapter(Duration.class, new GsonDurationAdapter()).
            create();

    public Extradition(TaskManager manager) {
        this.manager = manager;
    }

    public void getTasks(HttpExchange exchange) {
        String uri = exchange.getRequestURI().getPath();
        String[] path = uri.split("/");
        String query = exchange.getRequestURI().getQuery();

        if (query != null) {
            String[] parameter = query.split("=");
            try {
                int id = Integer.parseInt(parameter[1]);
                if ("task".equals(path[2])) {
                    sendOutput(exchange, manager.getTaskById(id), 200);
                }
            } catch (NumberFormatException e) {
                System.out.println("Не удается распознать ID");
            }

        } else {
            if ("task".equals(path[2])) {
                List<Task> subtasks = manager.getListOfAllTasks();
                sendOutput(exchange, subtasks, 200);
            }
        }
    }

    public void getSubtasks(HttpExchange exchange) {
        String uri = exchange.getRequestURI().getPath();
        String[] path = uri.split("/");
        String query = exchange.getRequestURI().getQuery();

        if (query != null) {
            String[] parameter = query.split("=");
            try {
                int id = Integer.parseInt(parameter[1]);
                if ("subtask".equals(path[2])) {
                    sendOutput(exchange, manager.getSubtaskById(id), 200);
                }
            } catch (NumberFormatException e) {
                System.out.println("Не удается распознать ID");
            }

        } else {
            if ("subtask".equals(path[2])) {
                List<Subtask> subtasks = manager.getListOfAllSubtasks();
                sendOutput(exchange, subtasks, 200);
            }
        }
    }

    public void getEpics(HttpExchange exchange) {
        String uri = exchange.getRequestURI().getPath();
        String[] path = uri.split("/");
        String query = exchange.getRequestURI().getQuery();

        if (query != null) {
            String[] parameter = query.split("=");
            try {
                int id = Integer.parseInt(parameter[1]);
                if ("epic".equals(path[2])) {
                    sendOutput(exchange, manager.getEpicById(id), 200);
                }
            } catch (NumberFormatException e) {
                System.out.println("Не удается распознать ID " + path[2]);
            }

        } else {
            if ("epic".equals(path[2])) {
                List<Epic> subtasks = manager.getListOfAllEpics();
                sendOutput(exchange, subtasks, 200);
            }
        }
    }

    public void postTasks(HttpExchange exchange) {
        String uri = exchange.getRequestURI().getPath();
        String[] path = uri.split("/");
        String query = exchange.getRequestURI().getQuery();

        if (query != null) {
            String[] parameter = query.split("=");
            try {
                int id = Integer.parseInt(parameter[1]);
                if ("task".equals(path[2])) {
                    manager.overwriteTask(id, getInputTaskWithId(exchange, Task.class));
                    exchange.close();
                } else if ("subtask".equals(path[2])) {
                    manager.overwriteSubtask(id, getInputTaskWithId(exchange, Subtask.class));
                    exchange.close();
                } else if ("epic".equals(path[2])) {
                    manager.overwriteEpic(id, getInputTaskWithId(exchange, Epic.class));
                    exchange.close();
                }
            } catch (NumberFormatException e) {
                System.out.println("Не удается распознать ID " + path[2]);
            }
        } else {
            if ("task".equals(path[2])) {
                manager.createATask(getInputTask(exchange, Task.class));
                exchange.close();
            } else if ("subtask".equals(path[2])) {
                manager.createASubtask(getInputTask(exchange, Subtask.class));
                exchange.close();
            } else if ("epic".equals(path[2])) {
                manager.createAEpic(getInputTask(exchange, Epic.class));
                exchange.close();
            }
        }
    }

    public void deleteTasks(HttpExchange exchange) {
        String uri = exchange.getRequestURI().getPath();
        String[] path = uri.split("/");
        String query = exchange.getRequestURI().getQuery();

        if (query != null) {
            String[] parameter = query.split("=");
            try {
                int id = Integer.parseInt(parameter[1]);
                System.out.println("2");
                if ("task".equals(path[2])) {
                    manager.deleteTask(id);
                    clearTasks(exchange);
                } else if ("subtask".equals(path[2])) {
                    manager.deleteSubtask(id);
                    clearTasks(exchange);

                } else if ("epic".equals(path[2])) {
                    manager.deleteEpic(id);
                    clearTasks(exchange);
                }
            } catch (NumberFormatException e) {
                System.out.println("Не удается распознать ID " + path[2]);
            }

        } else {
            if ("task".equals(path[2])) {
                manager.clearAllTask();
                clearTasks(exchange);

            } else if ("subtask".equals(path[2])) {
                manager.clearAllSubtask();
                clearTasks(exchange);

            } else if ("epic".equals(path[2])) {
                manager.clearAllEpic();
                clearTasks(exchange);
            }
        }
    }

    public void deleteAll(HttpExchange exchange) {
                manager.clearAllTask();
                manager.clearAllSubtask();
                manager.clearAllEpic();
                clearTasks(exchange);
    }

    public void getListOfAll(HttpExchange exchange) {

        List<Task> tasks = manager.getListOfAllTasks();
        List<Subtask> subtasks = manager.getListOfAllSubtasks();
        List<Epic> epics = manager.getListOfAllEpics();

        String responseTasks = gson.toJson(tasks);
        String responseSubtasks = gson.toJson(subtasks);
        String responseEpics = gson.toJson(epics);

        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, 0);
            os.write(responseTasks.getBytes());
            os.write(responseSubtasks.getBytes());
            os.write(responseEpics.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        exchange.close();
    }


    void getHistory(HttpExchange exchange) {
        List<Task> subtasks = manager.getHistory();
        sendOutput(exchange, subtasks, 200);
    }

    private void clearTasks(HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private <T> T getInputTaskWithId(HttpExchange exchange, Class<T> taskClass) {
        try (InputStream is = exchange.getRequestBody()) {
            exchange.sendResponseHeaders(201, 0);
            return gson.fromJson(new String(is.readAllBytes(),
                    StandardCharsets.UTF_8), taskClass);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private <T> T getInputTask(HttpExchange exchange, Class<T> taskClass) {
        try (InputStream is = exchange.getRequestBody()) {
            exchange.sendResponseHeaders(201, 0);
            return gson.fromJson(new String(is.readAllBytes(),
                    StandardCharsets.UTF_8), taskClass);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void sendOutput(HttpExchange exchange, Object data, int code) {
        String response = gson.toJson(data);

        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(code, response.getBytes().length);
            os.write(response.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
