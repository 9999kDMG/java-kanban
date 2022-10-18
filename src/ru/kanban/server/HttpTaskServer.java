package ru.kanban.server;

import com.sun.net.httpserver.HttpServer;
import ru.kanban.managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(ServerSettings.HTTP_TASK_SERVER_PORT), 0);
        server.createContext("/tasks", new TasksHandler(taskManager));
        System.out.println("HttpServer start at " + ServerSettings.HTTP_TASK_SERVER_PORT + " port");
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
