import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    HttpServer server;
    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TasksHandler(taskManager));
        System.out.println("HttpServer start at 8080 port");
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
