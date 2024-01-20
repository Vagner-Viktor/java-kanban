package managers;

import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "TaskManager is null");
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubtasks().size(), "Неверное количество подзадач.");
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество эпиков.");
        assertEquals(0, taskManager.getHistory().size(), "Неверное количество задач в истории.");
        assertEquals(1, taskManager.getIdCont(), "Неверный начальный индекс задач");
    }

    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "HistoryManager is null");
        assertEquals(0, historyManager.getHistory().size(), "Неверное количество задач в истории.");
    }
}