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

public class SubtasksHandlerTest {
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
                "  \"epicId\": 1,\n" +
                "  \"id\": 2,\n" +
                "  \"taskName\": \"Новая сабтаска\",\n" +
                "  \"description\": \"Описание новой задачи\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": null,\n" +
                "  \"startTime\": null\n" +
                "}";
        String bodyUpdateTest = "{\n" +
                "  \"epicId\": 1,\n" +
                "  \"id\": 2,\n" +
                "  \"taskName\": \"Обновлённая сабтаска\",\n" +
                "  \"description\": \"Описание новой задачи\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": null,\n" +
                "  \"startTime\": null\n" +
                "}";
        String body = "{\n" +
                "  \"taskName\": \"Новая задача\",\n" +
                "  \"description\": \"Описание новой задачи\"\n" +
                "}";
        URI urpEpic = URI.create("http://localhost:8080/epics");
        URI url = URI.create("http://localhost:8080/subtasks");
        URI urlUpdate = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest requestPostEpic = HttpRequest.newBuilder()
                .uri(urpEpic)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(bodyTest))
                .build();
        HttpRequest requestUpdate = HttpRequest.newBuilder()
                .uri(urlUpdate)
                .POST(HttpRequest.BodyPublishers.ofString(bodyUpdateTest))
                .build();
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(urlUpdate)
                .GET()
                .build();
        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(urlUpdate)
                .DELETE()
                .build();
        assertDoesNotThrow(() -> {
            client.send(requestPostEpic, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(response.statusCode(), 200, "Ошибка при создание таски");
            assertEquals(bodyTest, gson.toJson(HttpTaskServer.getManager().getSubtask(2)),
                    "Ошибка при проверке созданного эпика");
            HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(responseGet.body(), bodyTest, "Ошибка Get в SubtasksHandler");
            client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());
            assertEquals(bodyUpdateTest, gson.toJson(HttpTaskServer.getManager().getSubtask(2)), "Ошибка при обновлении таски");
            client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
//            assertEquals(HttpTaskServer.getManager().getSubtasks().toString(), "[]", "Ошибка Delete d SubtasksHandler");
        }, "Ошибка сервера в тесте checkPostFunctionOfHandler");
    }



}
