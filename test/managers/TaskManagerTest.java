package managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void addDifferentTypesOfTasksAndFindThemById() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        task.setStartTime(LocalDateTime.of(2022, 03, 01, 10, 00));
        task.setDuration(Duration.ofMinutes(60));
        final long taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final long epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        subtask.setStartTime(LocalDateTime.of(2022, 03, 02, 10, 00));
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
        int tasksCount = taskManager.getTasks().size();
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        task.setStartTime(LocalDateTime.of(2022, 03, 01, 10, 00));
        task.setDuration(Duration.ofMinutes(60));
        final long taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();
        Optional<Task> task2 = tasks.stream()
                .filter(task3 -> task3.getId() == taskId)
                .findFirst();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(tasksCount + 1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, task2.get(), "Задачи не совпадают.");
    }

    @Test
    void addEpic() {
        int epicsCount = taskManager.getEpics().size();
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final long epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        subtask.setStartTime(LocalDateTime.of(2022, 03, 01, 10, 00));
        subtask.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(subtask, epicId);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();
        Optional<Epic> epic2 = epics.stream()
                .filter(epic3 -> epic3.getId() == epicId)
                .findFirst();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(epicsCount + 1, epics.size(), "Неверное количество Эпиков.");
        assertEquals(epic, epic2.get(), "Эпики не совпадают.");
    }

    @Test
    void addSubtask() {
        int subtasksCount = taskManager.getSubtasks().size();
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final long epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description");
        subtask.setStartTime(LocalDateTime.of(2022, 03, 01, 10, 00));
        subtask.setDuration(Duration.ofMinutes(60));
        final long subtaskId = taskManager.addSubtask(subtask, epicId);
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();
        Optional<Subtask> subtask2 = subtasks.stream()
                .filter(subtask3 -> subtask3.getId() == subtaskId)
                .findFirst();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(subtasksCount + 1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtask2.get(), "Подзадачи не совпадают.");
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
        int taskCount = taskManager.getTasks().size();
        Task task = new Task("Test addNewTaskWithSameID", "Test addNewTaskWithSameID description");
        task.setStartTime(LocalDateTime.of(2022, 03, 01, 10, 00));
        task.setDuration(Duration.ofMinutes(60));
        final long taskId1 = taskManager.addTask(task);
        final Task savedTask1 = taskManager.getTask(taskId1);

        Task task2 = new Task("Test addNewTaskWithSameID2", "Test addNewTaskWithSameID2 description");
        task2.setStartTime(LocalDateTime.of(2022, 03, 02, 10, 00));
        task2.setDuration(Duration.ofMinutes(60));
        task2.setId(taskId1);
        final long taskId2 = taskManager.addTask(task2);
        final Task savedTask2 = taskManager.getTask(taskId2);

        assertNotEquals(taskId1, taskId2, "ID задач совпадают!");
        assertNotEquals(savedTask1, savedTask2, "Задачи совпадают!");
        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(taskCount + 2, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void checkForNotActualIdSubtasksInEpics() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final Long epicId = taskManager.addEpic(epic);
        Long[] subtasksListID = {200L, 300L, 400L, 500L, 600L};
        for (long l : subtasksListID) {
            Subtask subtask = new Subtask("Test addNewSubtask " + l, "Test addNewSubtask description " + l);
            subtask.setStartTime(LocalDateTime.of(2022, 03, 01, 10 + (int) l / 100, 00));
            subtask.setDuration(Duration.ofMinutes(60));
            subtask.setId(l);
            taskManager.addSubtask(subtask, epicId);
        }
        Long[] subtasksRemovedListID = {300L, 400L, 500L};
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
        int subtaskCount = taskManager.getSubtasks().size();
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final Long epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Test addNewSubtask 1", "Test addNewSubtask description 1");
        subtask1.setStartTime(LocalDateTime.of(2022, 03, 01, 10, 00));
        subtask1.setDuration(Duration.ofMinutes(60));
        long subtask1Id = taskManager.addSubtask(subtask1, epicId);

        Subtask subtask2 = new Subtask("Test addNewSubtask 2", "Test addNewSubtask description 2");
        subtask2.setStartTime(LocalDateTime.of(2022, 03, 01, 11, 00));
        subtask2.setDuration(Duration.ofMinutes(60));
        long subtask2Id = taskManager.addSubtask(subtask2, epicId);
        subtask2.setEpicId(1000L);
        taskManager.deleteSubtask(subtask2Id);
        assertEquals(subtaskCount + 2, taskManager.getSubtasks().size(), "Удаление подзадачи с неактуальным эпиком");
    }

    @Test
    void checkEpicStatusAllSubtasksNEW() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final Long epicId = taskManager.addEpic(epic);
        Long[] subtasksListID = {200L, 300L, 400L, 500L, 600L};
        for (long l : subtasksListID) {
            Subtask subtask = new Subtask("Test addNewSubtask " + l, "Test addNewSubtask description " + l);
            subtask.setStartTime(LocalDateTime.of(2022, 03, 01, 10 + (int) l / 100, 00));
            subtask.setDuration(Duration.ofMinutes(60));
            subtask.setId(l);
            subtask.setStatus(Status.NEW);
            taskManager.addSubtask(subtask, epicId);
        }
        assertEquals(Status.NEW, taskManager.getEpic(epicId).getStatus(), "Неверный статус эпика!");
    }

    @Test
    void checkEpicStatusAllSubtasksDONE() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final Long epicId = taskManager.addEpic(epic);
        Long[] subtasksListID = {200L, 300L, 400L, 500L, 600L};
        for (long l : subtasksListID) {
            Subtask subtask = new Subtask("Test addNewSubtask " + l, "Test addNewSubtask description " + l);
            subtask.setStartTime(LocalDateTime.of(2022, 03, 01, 10 + (int) l / 100, 00));
            subtask.setDuration(Duration.ofMinutes(60));
            subtask.setId(l);
            subtask.setStatus(Status.DONE);
            taskManager.addSubtask(subtask, epicId);
        }
        assertEquals(Status.DONE, taskManager.getEpic(epicId).getStatus(), "Неверный статус эпика!");
    }

    @Test
    void checkEpicStatusAllSubtasksNEWandDONE() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final Long epicId = taskManager.addEpic(epic);
        Long[] subtasksListID = {200L, 301L, 400L, 501L, 600L};
        for (long l : subtasksListID) {
            Subtask subtask = new Subtask("Test addNewSubtask " + l, "Test addNewSubtask description " + l);
            subtask.setStartTime(LocalDateTime.of(2022, 03, 01, 10 + (int) l / 100, 00));
            subtask.setDuration(Duration.ofMinutes(60));
            subtask.setId(l);
            if (l % 2 == 0) subtask.setStatus(Status.DONE);
            else subtask.setStatus(Status.NEW);
            taskManager.addSubtask(subtask, epicId);
        }
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epicId).getStatus(), "Неверный статус эпика!");
    }

    @Test
    void checkEpicStatusAllSubtasksNEWandDONEandIN_PROGRESS() {
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final Long epicId = taskManager.addEpic(epic);
        Long[] subtasksListID = {200L, 303L, 401L, 501L, 600L};
        for (long l : subtasksListID) {
            Subtask subtask = new Subtask("Test addNewSubtask " + l, "Test addNewSubtask description " + l);
            subtask.setStartTime(LocalDateTime.of(2022, 03, 01, 10 + (int) l / 100, 00));
            subtask.setDuration(Duration.ofMinutes(60));
            subtask.setId(l);
            if (l % 2 == 0) subtask.setStatus(Status.DONE);
            else if ((l % 3 == 0)) subtask.setStatus(Status.NEW);
            else subtask.setStatus(Status.IN_PROGRESS);
            taskManager.addSubtask(subtask, epicId);
        }
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epicId).getStatus(), "Неверный статус эпика!");
    }

    @Test
    void checkingTheIntersectionOfTasksFalse() {
        int subtaskPrioCount = taskManager.getPrioritizedTasks().size();
        Epic epic = new Epic("Test addNewEpicForSubtask", "Test addNewEpicForSubtask description");
        final Long epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Test addNewSubtask 1", "Test addNewSubtask description 1");
        subtask1.setStartTime(LocalDateTime.of(2020, 03, 01, 10, 00));
        subtask1.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(subtask1, epicId);

        Subtask subtask2 = new Subtask("Test addNewSubtask 2", "Test addNewSubtask description 2");
        subtask2.setStartTime(LocalDateTime.of(2020, 03, 01, 11, 00));
        subtask2.setDuration(Duration.ofMinutes(60));
         taskManager.addSubtask(subtask2, epicId);

        assertEquals(subtaskPrioCount + 2, taskManager.getPrioritizedTasks().size(), "Неверное количество приоритезированных задач");
        assertTrue(taskManager.getPrioritizedTasks().contains(subtask1), "Подзадачи нет в списке приоритезированных задач");
        assertTrue(taskManager.getPrioritizedTasks().contains(subtask2), "Подзадачи нет в списке приоритезированных задач");
    }
}
