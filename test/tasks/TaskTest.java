package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    void newTask() {
        String name = "TaskName";
        String description = "TaskDescription";
        Task task = new Task(name, description);

        assertEquals(task.getName(), name, "Название задачи не совпадает.");
        assertEquals(task.getDescription(), description, "Описание задачи не совпадает.");
        assertEquals(task.getStatus(), Status.NEW, "Cтатус задачи не верный.");
    }

    @Test
    void checkEqualsTask() {
        Task task1 = new Task("Test addNewTaskWithSameID", "Test addNewTaskWithSameID description");
        task1.setId(10L);
        Task task2 = new Task("Test addNewTaskWithSameID", "Test addNewTaskWithSameID description");
        task2.setId(10L);

        assertEquals(task1, task2, "Задачи не совпадают.");
    }

}