package utils;

import exceptions.TasksPriorityIntersection;
import tasks.Task;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PrioritizedTasks {
    private final Set<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> task1.getStartTime().compareTo(task2.getStartTime()));

    public void add(Task task) {
        try {
            if (!checkingTheIntersectionOfTasks(task)) prioritizedTasks.add(task);
        } catch (TasksPriorityIntersection e) {
            System.out.println("Задачи пересекаются! Добавляемая задача: \n" + task);
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

    public boolean checkingTheIntersectionOfTasks(Task task) {
        if ((task.getStartTime() == null) || (prioritizedTasks.stream()
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
