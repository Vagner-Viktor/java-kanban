package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;
import tasks.TaskTypes;

import java.lang.reflect.Type;
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
    void checkingTasksHistory() {
        final String taskName = "Test addNewTaskForHistory";
        final String taskDescription = "Test addNewTaskForHistory description";
        final Long taskId = 10L;
        Task task = new Task(taskName, taskDescription);
        task.setId(taskId);
        final Status taskStatus = task.getStatus();
        final TaskTypes taskType = task.getType();
        historyManager.add(task);

        task.setStatus(Status.IN_PROGRESS);
        task.setDescription("new description");
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(2, history.size(), "Количество записей в истории не корректно.");

        Task historyTask = history.get(0);
        assertEquals(historyTask.getName(), taskName,
                "Название первоначальной задачи и первой записи в истории не совпадают");
        assertEquals(historyTask.getDescription(), taskDescription,
                "Описание первоначальной задачи и первой записи в истории не совпадают");
        assertEquals(historyTask.getId(), taskId,
                "ID первоначальной задачи и первой записи в истории не совпадают");
        assertEquals(historyTask.getStatus(), taskStatus,
                "Статус первоначальной задачи и первой записи в истории не совпадают");
        assertEquals(historyTask.getType(), taskType,
                "Тип первоначальной задачи и первой записи в истории не совпадают");
    }

    @Test
    void checkHistorySize10() {
        Task task = new Task("Test addNewTaskForHistory", "Test addNewTaskForHistory description");
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(1, history.size(), "Информация в истории не корректна.");
        for (int i = 1; i < 20; i++) {
            historyManager.add(task);
        }
        assertEquals(10, history.size(), "В истории больше 10 задач!");
    }

}