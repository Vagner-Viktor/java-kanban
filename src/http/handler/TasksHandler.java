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
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static http.HttpTaskServer.DEFAULT_CHARSET;

public class TasksHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    enum TaskEndpoint {
        GET_TASKS,
        GET_TASK_BY_ID,
        CREATE_UPDATE_TASK,
        DELETE_TASK,
        UNKNOWN
    }

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TaskEndpoint taskEndpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (taskEndpoint) {
            case GET_TASKS:
                handleGetTasks(exchange);
                break;
            case GET_TASK_BY_ID:
                handleGetTaskById(exchange);
                break;
            case CREATE_UPDATE_TASK:
                handleCreateUpdateTask(exchange);
                break;
            case DELETE_TASK:
                handleDeleteTask(exchange);
                break;
            default:
                HttpTaskServer.writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private TaskEndpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (!pathParts[1].equals("api")
                || !pathParts[2].equals("v1")
                || !pathParts[3].equals("tasks"))
            return TaskEndpoint.UNKNOWN;
        switch (requestMethod) {
            case "GET":
                if (pathParts.length == 4) {
                    return TaskEndpoint.GET_TASKS;
                }
                if (pathParts.length == 5) {
                    return TaskEndpoint.GET_TASK_BY_ID;
                }
                break;
            case "POST":
                if (pathParts.length == 4) {
                    return TaskEndpoint.CREATE_UPDATE_TASK;
                }
                break;
            case "DELETE":
                if (pathParts.length == 5) {
                    return TaskEndpoint.DELETE_TASK;
                }
        }
        return TaskEndpoint.UNKNOWN;
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        HttpTaskServer.writeResponse(exchange, gson.toJson(taskManager.getTasks()), 200);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        Long taskId = getTaskId(exchange);
        if (taskId != null && taskManager.getTask(taskId) != null) {
            Task task = taskManager.getTask(taskId);
            HttpTaskServer.writeResponse(exchange, gson.toJson(task), 200);
        } else HttpTaskServer.writeResponse(exchange, "Некорректный идентификатор задачи", 404);
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        Long taskId = getTaskId(exchange);
        if (taskId != null && taskManager.getTask(taskId) != null) {
            taskManager.deleteTask(taskId);
            HttpTaskServer.writeResponse(exchange, "Задача с ID=" + taskId + " удалена!", 200);
        } else HttpTaskServer.writeResponse(exchange, "Некорректный идентификатор задачи", 404);
    }

    private void handleCreateUpdateTask(HttpExchange exchange) throws IOException {
        Optional<Task> parseTask = parseTask(exchange.getRequestBody());
        if (parseTask.isEmpty()) {
            HttpTaskServer.writeResponse(exchange, "Неудалось распарсить задачу", 400);
            return;
        }
        try {
            Task task = parseTask.get();
            if (taskManager.getTask(task.getId()) != null) {
                taskManager.updateTask(task);
                HttpTaskServer.writeResponse(exchange, "Задача с ID=" + task.getId() + " обновлена", 201);
            } else if (!taskManager.checkTheIntersectionOfTasks(task)) {
                Long taskId = taskManager.addTask(task);
                HttpTaskServer.writeResponse(exchange, "Задача с ID=" + taskId + " добавлена", 201);
            }
        } catch (TasksPriorityIntersection e) {
            HttpTaskServer.writeResponse(exchange, "Задачи пересекаются!", 406);
        }
    }

    private Optional<Task> parseTask(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), DEFAULT_CHARSET);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (jsonElement.isJsonObject()) {
            Task taskFromHttp = gson.fromJson(body, new TypeToken<Task>() {
            }.getType());
            return Optional.of(taskFromHttp);
        }
        return Optional.empty();
    }

    private Long getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            Optional<Long> idOpt = Optional.of(Long.parseLong(pathParts[4]));
            return idOpt.get();
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
