package sprint.servers;

import com.sun.net.httpserver.HttpServer;
import sprint.handlers.*;
import sprint.managers.InMemoryTaskManager;
import sprint.managers.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private static InMemoryTaskManager manager = (new Managers()).getDefault();
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        start(httpServer);
    }

    public static void start(HttpServer httpServer) throws IOException {
        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/epics", new EpicsHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        httpServer.start();
    }

    public static void stop(HttpServer httpServer) {
        httpServer.stop(0);
    }

    public static InMemoryTaskManager getManager() {
        return manager;
    }
}
