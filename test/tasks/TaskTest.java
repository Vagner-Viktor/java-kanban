package tasks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

@Test
    void newTask(){
    String name = "TaskName";
    String description = "TaskDescription";
    Task task = new Task(name, description);

    assertEquals(task.getName(), name, "Название задачи не совпадает.");
    assertEquals(task.getDescription(), description, "Описание задачи не совпадает.");
    assertEquals(task.getStatus(), Status.NEW, "Cтатус задачи не верный.");
}
}