package sprint.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sprint.adapters.DurationTypeAdapter;
import sprint.adapters.LocalDateTimeAdapter;
import sprint.exceptions.ValidateException;
import sprint.managers.InMemoryTaskManager;
import sprint.models.Task;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class TasksHandler implements HttpHandler {
    InMemoryTaskManager manager;
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    public TasksHandler(InMemoryTaskManager manager) {
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
                    Task task = gson.fromJson(body, Task.class);
                    Task taskPost;
                    if (Pattern.matches("^/tasks$", path)) {
                        if (task.getDuration() != null) {
                            taskPost = new Task(task.getTaskName(), task.getDescription(), (int) task.getDuration().toMinutes(), task.getStartTime());
                        } else {
                            taskPost = new Task(task.getTaskName(), task.getDescription());
                        }
                        manager.createTask(taskPost);
                    } else if (Pattern.matches("^/tasks/\\d+$", path)) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        if (task.getDuration() != null) {
                            taskPost = new Task(id, task.getTaskName(), task.getDescription(),
                                    task.getStatus(), (int) task.getDuration().toMinutes(), task.getStartTime());
                        } else {
                            taskPost = new Task(id, task.getTaskName(), task.getDescription(),
                                    task.getStatus());
                        }
                        manager.updateTask(taskPost);
                    }
                    sendResponse(httpExchange, 200, body);
                } catch (ValidateException e) {
                    sendResponse(httpExchange, 406, "Error of the validation");
                } catch (IOException e) {
                    sendResponse(httpExchange, 500, "Error 500");
                }
                break;
            case "GET":
                if (Pattern.matches("^/tasks$", path)) {
                    sendResponse(httpExchange, 200, gson.toJson(manager.getTasks()));
                } else if (Pattern.matches("^/tasks/\\d+$", path)) {
                    try {
                        int id = Integer.parseInt(path.split("/")[2]);

                        Task taskGet = manager.getTask(id);
                        sendResponse(httpExchange, 200, gson.toJson(taskGet));
                    } catch (NullPointerException e) {
                        sendResponse(httpExchange, 404, "Task not found");
                    }
                }
                break;
            case "DELETE":
                if (Pattern.matches("^/tasks/\\d+$", path)) {
                    int id = Integer.parseInt(path.split("/")[2]);
                    manager.removeTask(id);
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
