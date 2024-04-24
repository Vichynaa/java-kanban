package sprint.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sprint.adapters.DurationTypeAdapter;
import sprint.adapters.LocalDateTimeAdapter;
import sprint.managers.InMemoryTaskManager;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class HistoryHandler implements HttpHandler {
    InMemoryTaskManager manager;
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    public HistoryHandler(InMemoryTaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        switch (method) {
            case "GET":
                if (Pattern.matches("^/history$", path)) {
                    sendResponse(httpExchange, 200, gson.toJson(manager.getHistory()));
                }
        }
        sendResponse(httpExchange, 500, "Error 500");
    }

    private void sendResponse(HttpExchange httpExchange, int statusCode, String response) throws IOException {
        httpExchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
