package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }


    @Test
    void addDifferentTypesOfTasksAndFindThemById() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        task.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        task.setDuration(Duration.ofMinutes(60));
        final long taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final long epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        subtask.setStartTime(LocalDateTime.of(2024,03,02,10,00));
        subtask.setDuration(Duration.ofMinutes(60));
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
        task.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        task.setDuration(Duration.ofMinutes(60));
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
        subtask.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        subtask.setDuration(Duration.ofMinutes(60));
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
        subtask.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        subtask.setDuration(Duration.ofMinutes(60));
        subtask.setId(epicId);
        final Long subtaskId = taskManager.addSubtask(subtask, epicId);
        assertNull(subtaskId, "В эпик добавлена подзадача с ID эпика.");
    }

    @Test
    void addSubtaskWithSubtaskEpicId() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final long epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        subtask.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        subtask.setDuration(Duration.ofMinutes(60));
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

    @Test
    void checkForNotActualIdSubtasksInEpics() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final Long epicId = taskManager.addEpic(epic);
        Long[] subtasksListID = {2L, 3L, 4L, 5L, 6L};
        for (long l : subtasksListID) {
            Subtask subtask = new Subtask("Test addNewSubtask " + l, "Test addNewSubtask description " + l);
            subtask.setStartTime(LocalDateTime.of(2024,03,01,10+(int)l,00));
            subtask.setDuration(Duration.ofMinutes(60));
            subtask.setId(l);
            taskManager.addSubtask(subtask, epicId);
        }
        Long[] subtasksRemovedListID = {3L, 4L, 5L};
        for (long l : subtasksRemovedListID) {
            taskManager.deleteSubtask(l);
        }
        final List<Subtask> subtasksInEpic = taskManager.getEpicSubtasks(epicId);
        assertNotNull(subtasksInEpic, "Подзадачи эпика не возвращаются.");
        assertEquals(2, subtasksInEpic.size(), "Неверное количество подзадач в эпике.");
        for (Subtask subtask : subtasksInEpic) {
            assertFalse(Arrays.asList(subtasksRemovedListID).contains(subtask.getId()),
                    "В подзадачах эпика есть удаленная задача id=" + subtask.getId());
        }
    }

    @Test
    void checkActualEpicIdWhenDelSubtask() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final Long epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Test addNewSubtask 1", "Test addNewSubtask description 1");
        subtask1.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        subtask1.setDuration(Duration.ofMinutes(60));
        long subtask1Id = taskManager.addSubtask(subtask1, epicId);

        Subtask subtask2 = new Subtask("Test addNewSubtask 2", "Test addNewSubtask description 2");
        subtask2.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        subtask2.setDuration(Duration.ofMinutes(60));
        long subtask2Id = taskManager.addSubtask(subtask2, epicId);
        subtask2.setEpicId(1000L);
        taskManager.deleteSubtask(subtask2Id);
        assertEquals(2, taskManager.getSubtasks().size(), "Удаление подзадачи с неактуальным эпиком");
    }
}