package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;
import tasks.TaskTypes;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

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
        task.setId(1L);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(1, history.size(), "Информация в истории не корректна.");

        Task task2 = new Task("Test addNewTaskForHistory2", "Test addNewTaskForHistory2 description");
        task2.setId(2L);
        historyManager.add(task2);

        assertEquals(task, historyManager.getHistory().get(0), "Первая задача в истории не сохранена.");
    }

    @Test
    void remove() {
        Task task = new Task("Test addNewTaskForHistory", "Test addNewTaskForHistory description");
        task.setId(1L);
        historyManager.add(task);

        Task task2 = new Task("Test addNewTaskForHistory2", "Test addNewTaskForHistory2 description");
        task2.setId(2L);
        historyManager.add(task2);

        Task task3 = new Task("Test addNewTaskForHistory3", "Test addNewTaskForHistory3 description");
        task3.setId(3L);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(3, history.size(), "Информация о размере истории не корректна.");

        historyManager.remove(2L);
        history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(2, history.size(), "Информация о размере истории не корректна.");
        assertFalse(history.contains(task2), "Удаленная задача id=2, найдена в истории!");

        historyManager.remove(3L);
        history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(1, history.size(), "Информация о размере истории не корректна.");
        assertFalse(history.contains(task3), "Удаленная задача id=3, найдена в истории!");

        historyManager.remove(1L);
        history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(0, history.size(), "Информация о размере истории не корректна.");
        assertFalse(history.contains(task), "Удаленная задача id=1, найдена в истории!");
    }

    @Test
    void checkingTasksHistory() {
        final String taskName = "Test addNewTaskForHistory";
        final String taskDescription = "Test addNewTaskForHistory description";
        final Long taskId = 10L;
        Task task = new Task(taskName, taskDescription);
        task.setId(taskId);
        historyManager.add(task);

        task.setStatus(Status.IN_PROGRESS);
        final String newTaskDescription = "Test addNewTaskForHistory description";
        task.setDescription(newTaskDescription);
        final TaskTypes taskType = task.getType();
        final Status taskStatus = task.getStatus();
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(1, history.size(), "Количество записей в истории не корректно.");

        Task historyTask = history.get(0);
        assertEquals(historyTask.getName(), taskName,
                "Название первоначальной задачи и первой записи в истории не совпадают");
        assertEquals(historyTask.getDescription(), newTaskDescription,
                "Описание первоначальной задачи и первой записи в истории не совпадают");
        assertEquals(historyTask.getId(), taskId,
                "ID первоначальной задачи и первой записи в истории не совпадают");
        assertEquals(historyTask.getStatus(), taskStatus,
                "Статус первоначальной задачи и первой записи в истории не совпадают");
        assertEquals(historyTask.getType(), taskType,
                "Тип первоначальной задачи и первой записи в истории не совпадают");
    }

    @Test
    void checkHistorySizeRandom1_100000() {
        Task task = new Task("Test addNewTaskForHistory", "Test addNewTaskForHistory description");
        Random random = new Random();
        final long historySize = random.nextInt(100_000)+1;
        for (long i = 1; i <= historySize; i++) {
            task.setId(i);
            historyManager.add(task);
        }
        final List<Task> history = historyManager.getHistory();
        assertEquals(historySize, history.size(), "В истории неверное количество задач!");
    }

}