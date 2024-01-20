package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void newEpic(){
        String name = "EpicName";
        String description = "EpicDescription";
        Epic epic = new Epic(name, description);

        assertEquals(epic.getName(), name, "Название эпика не совпадает.");
        assertEquals(epic.getDescription(), description, "Описание эпика не совпадает.");
        assertEquals(epic.getStatus(), Status.NEW, "Cтатус эпика не верный.");
    }
}