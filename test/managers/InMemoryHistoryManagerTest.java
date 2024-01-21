package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void add() {
        Task task = new Task("Test addNewTaskForHistory", "Test addNewTaskForHistory description");
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(1, history.size(), "Информация в истории не корректна.");

        Task task2 = new Task("Test addNewTaskForHistory2", "Test addNewTaskForHistory2 description");
        historyManager.add(task2);
        assertEquals(task, historyManager.getHistory().get(0), "Первая задача в истории не сохранена.");
    }

    @Test
    void checkHistorySize10() {
        Task task = new Task("Test addNewTaskForHistory", "Test addNewTaskForHistory description");
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(1, history.size(), "Информация в истории не корректна.");
        for (int i = 1; i<20; i++){
            historyManager.add(task);
        }
        assertEquals(10, history.size(), "В истории больше 10 задач!");
    }

}