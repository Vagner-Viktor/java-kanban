package http.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static http.HttpTaskServer.DEFAULT_CHARSET;

public class EpicsHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    enum EpicEndpoint {
        GET_EPICS,
        GET_EPIC_BY_ID,
        GET_EPIC_SUBTASKS,
        CREATE_UPDATE_EPIC,
        DELETE_EPIC,
        UNKNOWN
    }

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EpicEndpoint epicEndpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (epicEndpoint) {
            case GET_EPICS:
                handleGetEpics(exchange);
                break;
            case GET_EPIC_BY_ID:
                handleGetEpicById(exchange);
                break;
            case GET_EPIC_SUBTASKS:
                handleGetEpicSubtasks(exchange);
                break;
            case CREATE_UPDATE_EPIC:
                handleCreateUpdateEpic(exchange);
                break;
            case DELETE_EPIC:
                handleDeleteEpic(exchange);
                break;
            default:
                HttpTaskServer.writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private EpicEndpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (!pathParts[1].equals("api")
                || !pathParts[2].equals("v1")
                || !pathParts[3].equals("epics"))
            return EpicEndpoint.UNKNOWN;
        switch (requestMethod) {
            case "GET":
                if (pathParts.length == 4) {
                    return EpicEndpoint.GET_EPICS;
                }
                if (pathParts.length == 5) {
                    return EpicEndpoint.GET_EPIC_BY_ID;
                }
                if (pathParts.length == 6 && pathParts[5].equals("subtasks")) {
                    return EpicEndpoint.GET_EPIC_SUBTASKS;
                }
                break;
            case "POST":
                if (pathParts.length == 4) {
                    return EpicEndpoint.CREATE_UPDATE_EPIC;
                }
                break;
            case "DELETE":
                if (pathParts.length == 5) {
                    return EpicEndpoint.DELETE_EPIC;
                }
        }
        return EpicEndpoint.UNKNOWN;
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        HttpTaskServer.writeResponse(exchange, gson.toJson(taskManager.getEpics()), 200);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        Long epicId = getEpicId(exchange);
        if (epicId != null && taskManager.getEpic(epicId) != null) {
            Epic epic = taskManager.getEpic(epicId);
            HttpTaskServer.writeResponse(exchange, gson.toJson(epic), 200);
        } else HttpTaskServer.writeResponse(exchange, "Некорректный идентификатор эпика", 404);
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        Long epicId = getEpicId(exchange);
        if (epicId != null && taskManager.getEpic(epicId) != null) {
            HttpTaskServer.writeResponse(exchange, gson.toJson(taskManager.getEpicSubtasks(epicId)), 200);
        } else HttpTaskServer.writeResponse(exchange, "Некорректный идентификатор эпика", 404);
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        Long epicId = getEpicId(exchange);
        if (epicId != null && taskManager.getEpic(epicId) != null) {
            taskManager.deleteEpic(epicId);
            HttpTaskServer.writeResponse(exchange, "Эпик с ID=" + epicId + " удален!", 200);
        } else HttpTaskServer.writeResponse(exchange, "Некорректный идентификатор эпика", 404);
    }

    private void handleCreateUpdateEpic(HttpExchange exchange) throws IOException {
        Optional<Epic> parseEpic = parseEpic(exchange.getRequestBody());
        if (parseEpic.isEmpty()) {
            HttpTaskServer.writeResponse(exchange, "Неудалось распарсить эпик", 400);
            return;
        }
        Epic epic = parseEpic.get();
        if (taskManager.getEpic(epic.getId()) != null) {
            taskManager.updateEpic(epic);
            HttpTaskServer.writeResponse(exchange, "Эпик с ID=" + epic.getId() + " обновлен", 201);
        } else {
            Long epicId = taskManager.addEpic(epic);
            HttpTaskServer.writeResponse(exchange, "Эпик с ID=" + epicId + " добавлен", 201);
        }
    }

    private Optional<Epic> parseEpic(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), DEFAULT_CHARSET);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (jsonElement.isJsonObject()) {
            Epic epicFromHttp = gson.fromJson(body, new TypeToken<Epic>() {
            }.getType());
            return Optional.of(epicFromHttp);
        }
        return Optional.empty();
    }

    private Long getEpicId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            Optional<Long> idOpt = Optional.of(Long.parseLong(pathParts[4]));
            return idOpt.get();
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
