package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }


    @Test
    void addDifferentTypesOfTasksAndFindThemById() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        final long taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final long epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        final long subtaskId = taskManager.addSubtask(subtask, epicId);
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertNotNull(savedEpic, "Эпик не найден.");
        assertNotNull(savedSubtask, "Подзадача не найдена.");

        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void addTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        final long taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final long epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void addSubtask() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final long epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        final long subtaskId = taskManager.addSubtask(subtask, epicId);
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void addSubtaskWithEpicIdToEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final long epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        subtask.setId(epicId);
        final Long subtaskId = taskManager.addSubtask(subtask, epicId);
        assertNull(subtaskId, "В эпик добавлена подзадача с ID эпика.");
    }

    @Test
    void addSubtaskWithSubtaskEpicId() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final long epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        final Long subtaskId = taskManager.addSubtask(subtask, subtask.getId());
        assertNull(subtaskId, "Добавлена сабтаска с ID самой сабтаски.");
    }

    @Test
    void checkThatTasksWithTheGivenIdAndTheGeneratedIdDoNotConflict() {
        Task task = new Task("Test addNewTaskWithSameID", "Test addNewTaskWithSameID description");
        final long taskId1 = taskManager.addTask(task);
        final Task savedTask1 = taskManager.getTask(taskId1);

        Task task2 = new Task("Test addNewTaskWithSameID2", "Test addNewTaskWithSameID2 description");
        task2.setId(taskId1);
        final long taskId2 = taskManager.addTask(task2);
        final Task savedTask2 = taskManager.getTask(taskId2);

        assertNotEquals(taskId1, taskId2, "ID задач совпадают!");
        assertNotEquals(savedTask1, savedTask2, "Задачи совпадают!");
        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
    }
}