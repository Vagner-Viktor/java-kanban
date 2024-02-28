package utils;

import managers.HistoryManager;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class FormatterUtil {
    public static String historyToString(HistoryManager manager) {
        String result = "";
        for (Task task : manager.getHistory()) {
            result = result + task.getId() + ", ";
        }
        return result;
    }

    public static List<Long> historyFromString(String value) {
        String[] numbers = value.split(", ");
        List<Long> historyList = new ArrayList<>();
        for (String number : numbers) {
            if (!number.isBlank() && !number.isEmpty()) historyList.add(Long.parseLong(number));
        }
        return historyList;
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
                return task;
        }
        return null;
    }

    public static String toString(Task task) {
        String result;
        if (TaskTypes.SUBTASK.equals(task.getType())) {
            Subtask subtask = (Subtask) task;
            result = subtask.getId() + ", " +
                    subtask.getType() + ", " +
                    subtask.getName() + ", " +
                    subtask.getStatus() + ", " +
                    subtask.getDescription() + ", " +
                    subtask.getEpicId();
        } else {
            result = task.getId() + ", " +
                    task.getType() + ", " +
                    task.getName() + ", " +
                    task.getStatus() + ", " +
                    task.getDescription();
        }
        return result;
    }
}
