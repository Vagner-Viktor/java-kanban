package utils;

import exceptions.TasksPriorityIntersection;
import org.junit.jupiter.api.Test;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PrioritizedTasksTest {

    @Test
    void checkingTheIntersectionOfTasks() {
        PrioritizedTasks prioritizedTasks = new PrioritizedTasks();
        Subtask subtask1 = new Subtask("Test addNewSubtask 1", "Test addNewSubtask description 1");
        subtask1.setStartTime(LocalDateTime.of(2020, 03, 01, 10, 00));
        subtask1.setDuration(Duration.ofMinutes(120));
        prioritizedTasks.add(subtask1);

        Subtask subtask2 = new Subtask("Test addNewSubtask 2", "Test addNewSubtask description 2");
        subtask2.setStartTime(LocalDateTime.of(2020, 03, 01, 11, 00));
        subtask2.setDuration(Duration.ofMinutes(60));

        assertThrows(TasksPriorityIntersection.class, () -> {
            prioritizedTasks.checkingTheIntersectionOfTasks(subtask2);
        }, "Пересечение задач по времени!");
    }

}