package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateTimeAdapter;
import http.handler.*;
import managers.FileBackedTaskManager;
import managers.Managers;
import managers.TaskManager;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static HttpServer httpServer;
    private static TaskManager taskManager;
    private static Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        HttpTaskServer.taskManager = taskManager;
        initialization();
    }

    public static void main(String[] args) throws IOException {
        taskManager = Managers.getDefault();
        taskManager = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));
        initialization();
        start();
    }

    public static void initialization() throws IOException {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/api/v1/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/api/v1/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/api/v1/subtask", new SubtasksHandler(taskManager));
        httpServer.createContext("/api/v1/history", new HistoryHandler(taskManager));
        httpServer.createContext("/api/v1/prioritized", new PrioritizedHandler(taskManager));
    }

    public static void start() {
        httpServer.start();
    }

    public static void stop(int delay) {
        httpServer.stop(delay);
    }

    public static Gson getGson() {
        return gson;
    }

    public static void writeResponse(HttpExchange exchange,
                                     String responseString,
                                     int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }
}
