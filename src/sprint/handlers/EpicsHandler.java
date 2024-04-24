package sprint.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sprint.adapters.DurationTypeAdapter;
import sprint.adapters.LocalDateTimeAdapter;
import sprint.exceptions.ValidateException;
import sprint.managers.InMemoryTaskManager;
import sprint.models.Epic;
import sprint.models.Subtask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

public class EpicsHandler implements HttpHandler {
    InMemoryTaskManager manager;
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    public EpicsHandler(InMemoryTaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        switch (method) {
            case "POST":
                try {
                    Epic epic = gson.fromJson(body, Epic.class);
                    Epic epicPost;
                    if (Pattern.matches("^/epics$", path)) {
                        epicPost = new Epic(epic.getTaskName(), epic.getDescription());
                        manager.createEpic(epicPost);
                    } else if (Pattern.matches("^/epics/\\d+$", path)) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        manager.updateEpic(new Epic(id, epic.getTaskName(), epic.getDescription()));
                    }
                    sendResponse(httpExchange, 200, body);
                } catch (ValidateException e) {
                    sendResponse(httpExchange, 406, "Error of the validation");
                } catch (IOException e) {
                    sendResponse(httpExchange, 500, "Error 500");
                }
                break;
            case "GET":
                if (Pattern.matches("^/epics$", path)) {
                    sendResponse(httpExchange, 200, gson.toJson(manager.getEpics()));
                } else if (Pattern.matches("^/epics/\\d+$", path)) {
                    try {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Epic epicGet = manager.getEpic(id);
                        sendResponse(httpExchange, 200, gson.toJson(epicGet));
                    } catch (NullPointerException e) {
                        sendResponse(httpExchange, 404, "Epic not found");
                    }
                } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                    try {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Epic epicGet = manager.getEpic(id);
                        List<Subtask> epicSubtasks = manager.getEpicSubtasks(epicGet);
                        sendResponse(httpExchange, 200, gson.toJson(epicSubtasks));
                    } catch (NullPointerException e) {
                        sendResponse(httpExchange, 404, "Epic not found");
                    }
                }
                break;
            case "DELETE":
                if (Pattern.matches("^/epics/\\d+$", path)) {
                    int id = Integer.parseInt(path.split("/")[2]);
                    manager.removeEpic(id);
                    sendResponse(httpExchange, 200, "Delete - correct");
                }
                break;
            default:
                sendResponse(httpExchange, 500, "Error 500");
        }
    }

    private void sendResponse(HttpExchange httpExchange, int statusCode, String response) throws IOException {
        httpExchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
