package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void newSubtask(){
        String name = "SubtaskName";
        String description = "SubtaskDescription";
        Subtask subtask = new Subtask(name, description);

        assertEquals(subtask.getName(), name, "Название подзадачи не совпадает.");
        assertEquals(subtask.getDescription(), description, "Описание подзадачи не совпадает.");
        assertEquals(subtask.getStatus(), Status.NEW, "Cтатус подзадачи не верный.");
    }

}