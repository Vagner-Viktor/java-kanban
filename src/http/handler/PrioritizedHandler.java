package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import managers.TaskManager;

import java.io.IOException;

public class PrioritizedHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (exchange.getRequestMethod().equals("GET")
                && pathParts[1].equals("api")
                && pathParts[2].equals("v1")
                && pathParts.length == 4
                && pathParts[3].equals("prioritized"))
            HttpTaskServer.writeResponse(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        else HttpTaskServer.writeResponse(exchange, "Такого эндпоинта не существует", 404);
    }
}
