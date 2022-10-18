package ru.kanban.server;

import com.google.gson.Gson;
import ru.kanban.utils.GsonMaker;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final String ServerURL = "http://localhost:" + ServerSettings.KV_SERVER_PORT;
    private String token;
    private final Gson GSON = GsonMaker.getGson();

    public KVTaskClient() {
        token = getServerToken();
    }

    private String getServerToken() {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(ServerURL + "/register");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            token = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Не удалось получить токен" + e.getMessage());
        }
        return token;
    }

    public void save(String key, String value) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(ServerURL + "/save/" + key + "?API_TOKEN=" + token);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value, DEFAULT_CHARSET);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(ServerURL + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}

