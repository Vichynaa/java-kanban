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

public class EpicsHandlerTest {
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
    void checkFunctionOfEpicHandler() {
        String body = "{\n" +
                "  \"taskName\": \"Новая задача\",\n" +
                "  \"description\": \"Описание новой задачи\"\n" +
                "}";
        String bodyUpdate = "{\n" +
                "  \"taskName\": \"Обновлённая задача\",\n" +
                "  \"description\": \"Описание новой задачи\"\n" +
                "}";
        String bodyTest = "{\n" +
                "  \"subtasks\": [],\n" +
                "  \"endTime\": null,\n" +
                "  \"id\": 1,\n" +
                "  \"taskName\": \"Новая задача\",\n" +
                "  \"description\": \"Описание новой задачи\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": null,\n" +
                "  \"startTime\": null\n" +
                "}";
        String bodyUpdateTest = "{\n" +
                "  \"subtasks\": [],\n" +
                "  \"endTime\": null,\n" +
                "  \"id\": 1,\n" +
                "  \"taskName\": \"Обновлённая задача\",\n" +
                "  \"description\": \"Описание новой задачи\",\n" +
                "  \"status\": \"NEW\",\n" +
                "  \"duration\": null,\n" +
                "  \"startTime\": null\n" +
                "}";
        URI url = URI.create("http://localhost:8080/epics");
        URI urlUpdate = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpRequest requestUpdate = HttpRequest.newBuilder()
                .uri(urlUpdate)
                .POST(HttpRequest.BodyPublishers.ofString(bodyUpdate))
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
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(response.statusCode(), 200, "Ошибка при создание эпика");
            assertEquals(bodyTest, gson.toJson(HttpTaskServer.getManager().getEpic(1)),
                    "Ошибка при проверке созданного эпика");
            HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
            assertEquals(responseGet.body(), bodyTest, "Ошибка Get в EpicsHandler");
            client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());
            assertEquals(bodyUpdateTest, gson.toJson(HttpTaskServer.getManager().getEpic(1)), "Ошибка при обновлении эпика");
            client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
            assertEquals(HttpTaskServer.getManager().getEpics().toString(), "[]", "Ошибка Delete d EpicsHandler");
        }, "Ошибка сервера в тесте checkPostFunctionOfHandler");
    }



}
