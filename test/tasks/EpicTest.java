package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    void newEpic() {
        String name = "EpicName";
        String description = "EpicDescription";
        Epic epic = new Epic(name, description);

        assertEquals(epic.getName(), name, "Название эпика не совпадает.");
        assertEquals(epic.getDescription(), description, "Описание эпика не совпадает.");
        assertEquals(epic.getStatus(), Status.NEW, "Cтатус эпика не верный.");
    }

    @Test
    void checkEqualsEpic() {
        Epic epic1 = new Epic("Test addNewEpicWithSameID", "Test addNewEpicWithSameID description");
        epic1.setId(10L);
        Epic epic2 = new Epic("Test addNewEpicWithSameID", "Test addNewEpicWithSameID description");
        epic2.setId(10L);

        assertEquals(epic1, epic2, "Эпики не совпадают.");
    }

    @Test
    void addSubtaskWithEpicIdToEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final long epicId = 10;
        epic.setId(epicId);
        epic.addSubtask(epicId);
        assertEquals(0, epic.getSubtaskList().size(), "В эпик добавлена подзадача с ID эпика.");
    }
}