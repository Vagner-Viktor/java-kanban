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
    @Test
    void checkEqualsSubtask() {
        Subtask subtask1 = new Subtask("Test addNewSubtaskWithSameID", "Test addNewSubtaskWithSameID description");
        subtask1.setId(10L);
        Subtask subtask2 = new Subtask("Test addNewSubtaskWithSameID", "Test addNewSubtaskWithSameID description");
        subtask2.setId(10L);

        assertEquals(subtask1, subtask2, "Подзадачи не совпадают.");
    }

    @Test
    void setSubtaskEpicIdWithYourSubtaskId() {
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        final Long subtaskId = 10L;
        subtask.setId(subtaskId);
        subtask.setEpicId(subtaskId);
        assertNotEquals(subtask.getId(), subtask.getEpicId(),
                "ID сабтаски установлен в качестве ID эпика самой сабтаски.");
    }
}