package utils;

import exceptions.TasksPriorityIntersection;
import tasks.Task;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class PrioritizedTasks {
    private final Set<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> task1.getStartTime().compareTo(task2.getStartTime()));
    private static final Logger logger = Logger.getLogger(PrioritizedTasks.class.getName());

    public void add(Task task) {
        try {
            if (!checkTheIntersectionOfTasks(task) && task.getStartTime() != null) prioritizedTasks.add(task);
        } catch (TasksPriorityIntersection e) {
            logger.info("Задачи пересекаются! Добавляемая задача: \n" + task);
        }
    }

    public void update(Task oldTask, Task newTask) {
        remove(oldTask);
        add(newTask);
    }

    public void remove(Task task) {
        prioritizedTasks.remove(task);
    }

    public void remove(List<Task> tasks) {
        tasks.stream()
                .forEach(task -> prioritizedTasks.remove(task));
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public boolean checkTheIntersectionOfTasks(Task task) {
        if (task.getStartTime() == null) return false;
        if ((prioritizedTasks.stream()
                .filter(task2 -> ((task2.getStartTime().isBefore(task.getStartTime()) &&
                        task2.getStartTime().plus(task2.getDuration()).isAfter(task.getStartTime())) ||
                        task2.getStartTime().equals(task.getStartTime())))
                .findFirst()
                .isPresent())) {
            throw new TasksPriorityIntersection();
        }
        return false;
    }
}
