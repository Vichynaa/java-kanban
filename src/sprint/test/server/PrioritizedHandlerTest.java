package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint.adapters.DurationTypeAdapter;
import sprint.adapters.LocalDateTimeAdapter;
import sprint.managers.InMemoryTaskManager;
import sprint.managers.Managers;
import sprint.models.Task;
import sprint.servers.HttpTaskServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedHandlerTest {
    final HttpClient client = HttpClient.newBuilder().build();
    HttpServer httpServer;
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    @BeforeEach
    void beforeEach() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpTaskServer.start(httpServer);
        InMemoryTaskManager.setId(0);

    }

    @AfterEach
    void afterEach() {
        HttpTaskServer.stop(httpServer);
    }

    @Test
    void checkFunctionOfTaskHandler() {
        String bodyTest = "{\n" +
                "\t\"taskName\": \"1121231231231233123\",\n" +
                "\t\"description\": \"Описание новой задачи1\",\n" +
                "\t\"duration\": 60,\n" +
                "\t\"startTime\": \"2023-04-25T10:00:00\"\n" +
                "}";
        String prioritizedCheck = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"taskName\": \"1121231231231233123\",\n" +
                "    \"description\": \"Описание новой задачи1\",\n" +
                "    \"status\": \"NEW\",\n" +
                "    \"duration\": 60,\n" +
                "    \"startTime\": \"2023-04-25T10:00:00\"\n" +
                "  }\n" +
                "]";
        URI url = URI.create("http://localhost:8080/tasks");
        URI urlUpdate = URI.create("http://localhost:8080/tasks/1");
        URI history = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(bodyTest))
                .build();
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(urlUpdate)
                .GET()
                .build();
        HttpRequest requestPrioritized = HttpRequest.newBuilder()
                .uri(history)
                .GET()
                .build();
        assertDoesNotThrow(() -> {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            client.send(requestGet, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> responsePrioritized = client.send(requestPrioritized, HttpResponse.BodyHandlers.ofString());
            assertEquals(responsePrioritized.body(), prioritizedCheck, "Ошибка Get в PrioritizedHandler");
        }, "Ошибка сервера в тесте checkPostFunctionOfHandler");
    }



}
