package http.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TasksPriorityIntersection;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static http.HttpTaskServer.DEFAULT_CHARSET;

public class SubtasksHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    enum SubtaskEndpoint {
        GET_SUBTASKS,
        GET_SUBTASK_BY_ID,
        CREATE_UPDATE_SUBTASK,
        DELETE_SUBTASK,
        UNKNOWN
    }

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SubtaskEndpoint subtaskEndpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (subtaskEndpoint) {
            case GET_SUBTASKS:
                handleGetSubtasks(exchange);
                break;
            case GET_SUBTASK_BY_ID:
                handleGetSubtaskById(exchange);
                break;
            case CREATE_UPDATE_SUBTASK:
                handleCreateUpdateSubtask(exchange);
                break;
            case DELETE_SUBTASK:
                handleDeleteSubtask(exchange);
                break;
            default:
                HttpTaskServer.writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private SubtaskEndpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (!pathParts[1].equals("api")
                || !pathParts[2].equals("v1")
                || !pathParts[3].equals("subtasks"))
            return SubtaskEndpoint.UNKNOWN;
        switch (requestMethod) {
            case "GET":
                if (pathParts.length == 4) {
                    return SubtaskEndpoint.GET_SUBTASKS;
                }
                if (pathParts.length == 5) {
                    return SubtaskEndpoint.GET_SUBTASK_BY_ID;
                }
                break;
            case "POST":
                if (pathParts.length == 4) {
                    return SubtaskEndpoint.CREATE_UPDATE_SUBTASK;
                }
                break;
            case "DELETE":
                if (pathParts.length == 5) {
                    return SubtaskEndpoint.DELETE_SUBTASK;
                }
        }
        return SubtaskEndpoint.UNKNOWN;
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        HttpTaskServer.writeResponse(exchange, gson.toJson(taskManager.getSubtasks()), 200);
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        Long subtaskId = getSubtaskId(exchange);
        if (subtaskId != null && taskManager.getSubtask(subtaskId) != null) {
            Subtask subtask = taskManager.getSubtask(subtaskId);
            HttpTaskServer.writeResponse(exchange, gson.toJson(subtask), 200);
        } else HttpTaskServer.writeResponse(exchange, "Некорректный идентификатор подзадачи", 404);
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        Long subtaskId = getSubtaskId(exchange);
        if (subtaskId != null && taskManager.getSubtask(subtaskId) != null) {
            taskManager.deleteSubtask(subtaskId);
            HttpTaskServer.writeResponse(exchange, "Подзадача с ID=" + subtaskId + " удалена!", 200);
        } else HttpTaskServer.writeResponse(exchange, "Некорректный идентификатор подзадачи", 404);
    }

    private void handleCreateUpdateSubtask(HttpExchange exchange) throws IOException {
        Optional<Subtask> parseSubtask = parseSubtask(exchange.getRequestBody());
        if (parseSubtask.isEmpty()) {
            HttpTaskServer.writeResponse(exchange, "Неудалось распарсить подзадачу", 400);
            return;
        }
        try {
            Subtask subtask = parseSubtask.get();
            if (taskManager.getSubtask(subtask.getId()) != null) {
                taskManager.updateSubtask(subtask);
                HttpTaskServer.writeResponse(exchange, "Подзадача с ID=" + subtask.getId() + " обновлена", 201);
            } else if (!taskManager.checkTheIntersectionOfTasks(subtask)) {
                Long taskId = taskManager.addSubtask(subtask, subtask.getEpicId());
                HttpTaskServer.writeResponse(exchange, "Подзадача с ID=" + taskId + " добавлена", 201);
            }
        } catch (TasksPriorityIntersection e) {
            HttpTaskServer.writeResponse(exchange, "Задачи пересекаются!", 406);
        }
    }

    private Optional<Subtask> parseSubtask(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), DEFAULT_CHARSET);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (jsonElement.isJsonObject()) {
            Subtask subtaskFromHttp = gson.fromJson(body, new TypeToken<Subtask>() {
            }.getType());
            return Optional.of(subtaskFromHttp);
        }
        return Optional.empty();
    }

    private Long getSubtaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            Optional<Long> idOpt = Optional.of(Long.parseLong(pathParts[4]));
            return idOpt.get();
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}