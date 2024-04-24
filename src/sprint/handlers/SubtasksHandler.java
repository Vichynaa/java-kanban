package sprint.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sprint.adapters.DurationTypeAdapter;
import sprint.adapters.LocalDateTimeAdapter;
import sprint.exceptions.ValidateException;
import sprint.managers.InMemoryTaskManager;
import sprint.models.Subtask;
import sprint.models.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class SubtasksHandler implements HttpHandler {
    InMemoryTaskManager manager;
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    public SubtasksHandler(InMemoryTaskManager manager) {
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
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (subtask.getEpicId() == 0) {
                        sendResponse(httpExchange, 500, "Error: epicId is empty");
                        return;
                    }
                    Subtask subtaskPost;
                    if (Pattern.matches("^/subtasks$", path)) {
                        if (subtask.getDuration() != null) {
                            subtaskPost = new Subtask(subtask.getEpicId(), subtask.getTaskName(),
                                    subtask.getDescription(),
                                    (int) subtask.getDuration().toMinutes(), subtask.getStartTime());
                        } else {
                            subtaskPost = new Subtask(subtask.getEpicId(), subtask.getTaskName(), subtask.getDescription());
                        }
                        manager.createSubtask(subtaskPost);
                    } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        if (subtask.getDuration() != null) {
                            subtaskPost = new Subtask(subtask.getEpicId(), id, subtask.getTaskName(), subtask.getDescription(),
                                    subtask.getStatus(), (int) subtask.getDuration().toMinutes(), subtask.getStartTime());

                        }
                        else {
                            subtaskPost = new Subtask(subtask.getEpicId(), id, subtask.getTaskName(), subtask.getDescription(),
                                    subtask.getStatus());
                        }
                        manager.updateSubtask(subtaskPost);

                    }
                    sendResponse(httpExchange, 200, body);
                } catch (ValidateException e) {
                    sendResponse(httpExchange, 406, "Error of the validation");
                } catch (IOException e) {
                    sendResponse(httpExchange, 500, "Error 500");
                }
                break;
            case "GET":
                if (Pattern.matches("^/subtasks$", path)) {
                    sendResponse(httpExchange, 200, gson.toJson(manager.getSubtasks()));
                } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
                    try {
                        int id = Integer.parseInt(path.split("/")[2]);

                        Subtask subtaskGet = manager.getSubtask(id);
                        sendResponse(httpExchange, 200, gson.toJson(subtaskGet));
                    } catch (NullPointerException e) {
                        sendResponse(httpExchange, 404, "Subtask not found");
                    }
                }
                break;
            case "DELETE":
                if (Pattern.matches("^/subtasks/\\d+$", path)) {
                    sendResponse(httpExchange, 200, "Delete - correct");
                    int id = Integer.parseInt(path.split("/")[2]);
                    manager.removeSubtask(id);
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
