package utils;

import managers.HistoryManager;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FormatterUtil {
    public static String historyToString(HistoryManager manager) {
        return manager.getHistory().stream()
                .map(task -> task.getId().toString())
                .collect(Collectors.joining(", "));
    }

    public static List<Long> historyFromString(String value) {
        if (value == null) return new ArrayList<Long>();
        String[] numbers = value.split(", ");
        return Arrays.stream(numbers)
                .filter(number -> (!number.isBlank() && !number.isEmpty()))
                .map(number -> Long.parseLong(number))
                .collect(Collectors.toList());
    }

    public static Task fromString(String value) {
        String[] values = value.split(", ");
        switch (TaskTypes.valueOf(values[1])) {
            case SUBTASK:
                Subtask subtask = new Subtask(values[2], values[4]);
                subtask.setId(Long.parseLong(values[0]));
                subtask.setStatus(Status.valueOf(values[3]));
                subtask.setType(TaskTypes.valueOf(values[1]));
                subtask.setEpicId(Long.parseLong(values[5]));
                subtask.setStartTime(LocalDateTime.parse(values[6], Task.DATE_TIME_FORMATTER));
                subtask.setDuration(Duration.ofMinutes(Integer.parseInt(values[7])));
                return subtask;
            case EPIC:
                Epic epic = new Epic(values[2], values[4]);
                epic.setId(Long.parseLong(values[0]));
                epic.setStatus(Status.valueOf(values[3]));
                epic.setType(TaskTypes.valueOf(values[1]));
                return epic;
            case TASK:
                Task task = new Task(values[2], values[4]);
                task.setId(Long.parseLong(values[0]));
                task.setStatus(Status.valueOf(values[3]));
                task.setType(TaskTypes.valueOf(values[1]));
                task.setStartTime(LocalDateTime.parse(values[5], Task.DATE_TIME_FORMATTER));
                task.setDuration(Duration.ofMinutes(Integer.parseInt(values[6])));
                return task;
            default:
                return null;
        }
    }

    public static String toString(Task task) {
        String result;
        switch (task.getType()) {
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                result = subtask.getId() + ", " +
                        subtask.getType() + ", " +
                        subtask.getName() + ", " +
                        subtask.getStatus() + ", " +
                        subtask.getDescription() + ", " +
                        subtask.getEpicId() + ", " +
                        subtask.getStartTime().format(Task.DATE_TIME_FORMATTER) + ", " +
                        subtask.getDuration().toMinutes();
                return result;
            case EPIC:
                result = task.getId() + ", " +
                        task.getType() + ", " +
                        task.getName() + ", " +
                        task.getStatus() + ", " +
                        task.getDescription();
                return result;
            case TASK:
                result = task.getId() + ", " +
                        task.getType() + ", " +
                        task.getName() + ", " +
                        task.getStatus() + ", " +
                        task.getDescription() + ", " +
                        task.getStartTime().format(Task.DATE_TIME_FORMATTER) + ", " +
                        task.getDuration().toMinutes();
                return result;
            default:
                return null;
        }
    }
}
