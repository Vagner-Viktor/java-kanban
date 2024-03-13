package managers;

import exceptions.ManagerLoadEmptyException;
import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    TaskManager taskManager;
    File testFile;

    @BeforeEach
    void beforeEach() {
        taskManager = FileBackedTaskManager.loadFromFile(createTestFile());
    }

    @AfterEach
    void afterEach() throws ManagerSaveException {
        try {
            Files.deleteIfExists(Paths.get(testFile.toString()));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    File createTestFile() throws ManagerSaveException {
        try {
            testFile = File.createTempFile("tmp_tasks", ".csv");
            BufferedWriter fileWrite = new BufferedWriter(new FileWriter(testFile));
            fileWrite.write("id, type, name, status, description, epic, start, duration\n" +
                    "1, TASK, Вынести мусор, IN_PROGRESS, Вынести весь мусор после сортировки, 01.03.2024 10:00, 60\n" +
                    "2, EPIC, Поменять зимние шины на летние на автомобиле, NEW, Поменять шины в автомобиле с летних на зимние\n" +
                    "7, EPIC, Путешествие на выходные, IN_PROGRESS, Запланировать путешествие на выходные на машине\n" +
                    "12, EPIC, Перевести слова на английский, DONE, Только перевести слова с английского\n" +
                    "3, SUBTASK, Заказать зимние шины, NEW, Позвонить в фирму по хранению шин и заказать со склада зимние шины, 2, 02.03.2024 10:00, 60\n" +
                    "4, SUBTASK, Назначить встречу по замене шин, NEW, Договориться о времени замены шин, 2, 02.03.2024 11:00, 60\n" +
                    "5, SUBTASK, Договориться о хранении летних шин, NEW, Договориться о хранении летних шин и выяснить стоимость и сроки хранения, 2, 02.03.2024 12:00, 60\n" +
                    "6, SUBTASK, Провести диагностику шин, NEW, Проверить давление и провести балансировку, 2, 02.03.2024 13:00, 60\n" +
                    "8, SUBTASK, Выбрать город, NEW, Выбрать город в пределах 300км. где еще не были, 7, 03.03.2024 10:00, 60\n" +
                    "9, SUBTASK, Выбрать отель, IN_PROGRESS, Выбрать отель в центре с парковкой, 7, 03.03.2024 11:00, 60\n" +
                    "10, SUBTASK, Составить список достопримечательностей, NEW, Найти главные достопримечательности города и составить маршрут посещения, 7, 03.03.2024 12:00, 60\n" +
                    "11, SUBTASK, Выбрать рестораны, NEW, Определить список ресторанов по маршруту посещения достопримечательностей, 7, 03.03.2024 13:00, 60\n" +
                    "13, SUBTASK, Задача, DONE, Задача - Task, 12, 04.03.2024 10:00, 30\n" +
                    "14, SUBTASK, Подзадача, DONE, Подзадача - Subtask, 12, 04.03.2024 10:30, 30\n" +
                    "15, SUBTASK, Эпик, DONE, Эпик - Epic, 12, 04.03.2024 11:00, 60\n" +
                    "History\n" +
                    "9, 13, 14, 15, 12, 1, ");
            fileWrite.close();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return testFile;
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
        int tasksCount = taskManager.getTasks().size();
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        task.setStartTime(LocalDateTime.of(2024,03,01,10,00));
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
        subtask.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        subtask.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(subtask, epicId);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();
        Optional<Epic> epic2 = epics.stream()
                        .filter(epic3 -> epic3.getId() == epicId)
                        .findFirst();
        System.out.println(epics);
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
        subtask.setStartTime(LocalDateTime.of(2024,03,01,10,00));
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
        task.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        task.setDuration(Duration.ofMinutes(60));
        final long taskId1 = taskManager.addTask(task);
        final Task savedTask1 = taskManager.getTask(taskId1);

        Task task2 = new Task("Test addNewTaskWithSameID2", "Test addNewTaskWithSameID2 description");
        task2.setStartTime(LocalDateTime.of(2024,03,02,10,00));
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
            subtask.setStartTime(LocalDateTime.of(2024,03,01,10+(int)l/100,00));
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
        subtask1.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        subtask1.setDuration(Duration.ofMinutes(60));
        long subtask1Id = taskManager.addSubtask(subtask1, epicId);

        Subtask subtask2 = new Subtask("Test addNewSubtask 2", "Test addNewSubtask description 2");
        subtask2.setStartTime(LocalDateTime.of(2024,03,01,11,00));
        subtask2.setDuration(Duration.ofMinutes(60));
        long subtask2Id = taskManager.addSubtask(subtask2, epicId);
        subtask2.setEpicId(1000L);
        taskManager.deleteSubtask(subtask2Id);
        assertEquals(subtaskCount + 2, taskManager.getSubtasks().size(), "Удаление подзадачи с неактуальным эпиком");
    }

    @Test
    void loadAndCreateEmptyFile() throws ManagerSaveException, ManagerLoadException, IOException {
        File emptyFile = null;
        String result = "";
        try {
            emptyFile = File.createTempFile("empty", ".csv");
            TaskManager filetaskLoadManager = FileBackedTaskManager.loadFromFile(emptyFile);
        } catch (ManagerLoadException e) {
            result = "Ошибка при загрузке файла.";
        } catch (ManagerLoadEmptyException e) {
            result = "Ошибка при загрузке файла. Файл пуст.";
        } catch (IOException e) {
            throw new ManagerLoadException();
        } finally {
            Files.deleteIfExists(Paths.get(emptyFile.toString()));
        }
        assertEquals(result, "Ошибка при загрузке файла. Файл пуст.");

        TaskManager filetaskWriteManager = new FileBackedTaskManager(emptyFile);
        filetaskWriteManager.getTask(1L);
        assertEquals(true, Files.exists(Paths.get(emptyFile.toString())), "Пустой файл не создан.");
        Files.deleteIfExists(Paths.get(emptyFile.toString()));
    }

    @Test
    void saveNewTaskInFile() {
        try {
            Long linesInFile = Files.lines(Paths.get(testFile.toString())).count();

            Task task = new Task("Test addNewTask", "Test addNewTask description");
            task.setStartTime(LocalDateTime.of(2024,03,01,10,00));
            task.setDuration(Duration.ofMinutes(60));
            taskManager.addTask(task);
            Long newLinesInFile = Files.lines(Paths.get(testFile.toString())).count();
            assertEquals(newLinesInFile, linesInFile + 1, "Задача, добавленная в файл, не сохранена");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loadTasksFromFile() {
        assertEquals(1, taskManager.getTasks().size(), "Загружено неверное число задач");
        assertEquals(3, taskManager.getEpics().size(), "Загружено неверное число эпиков");
        assertEquals(11, taskManager.getSubtasks().size(), "Загружено неверное число подзадач");
        assertEquals(6, taskManager.getHistory().size(), "Загружена неверно история");
    }

    @Test
    void checkingSavingAndLoadingTasksInDifferentManagers() throws IOException {
        File emptyFile = null;
        try {
            emptyFile = File.createTempFile("empty_for_tasks", ".csv");
        } catch (IOException e) {
            throw new ManagerLoadException();
        }
        TaskManager taskManager1 = new FileBackedTaskManager(emptyFile);
        Task task = new Task("Test addNewTaskWithSameID", "Test addNewTaskWithSameID description");
        task.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        task.setDuration(Duration.ofMinutes(60));
        final long taskId1 = taskManager1.addTask(task);
        final Task savedTask1TM1 = taskManager1.getTask(taskId1);
        Task task2 = new Task("Test addNewTaskWithSameID2", "Test addNewTaskWithSameID2 description");
        task2.setStartTime(LocalDateTime.of(2024,03,02,10,00));
        task2.setDuration(Duration.ofMinutes(30));
        final long taskId2 = taskManager1.addTask(task2);
        final Task savedTask2TM1 = taskManager1.getTask(taskId2);
        final int tasksSizeTM1 = taskManager1.getTasks().size();
        final String historyTM1 = taskManager1.getHistory().toString();

        TaskManager taskManager2 = FileBackedTaskManager.loadFromFile(emptyFile);
        final Task savedTask1TM2 = taskManager2.getTask(taskId1);
        final Task savedTask2TM2 = taskManager2.getTask(taskId2);
        final int tasksSizeTM2 = taskManager2.getTasks().size();
        final String historyTM2 = taskManager2.getHistory().toString();

        assertEquals(savedTask1TM1, savedTask1TM2, "Задачи не совпадают!");
        assertEquals(savedTask2TM1, savedTask2TM2, "Задачи не совпадают!");
        assertEquals(tasksSizeTM1, tasksSizeTM2, "Количество задач не совпадает!");
        assertEquals(historyTM1, historyTM2, "История не совпадает!");

        Files.deleteIfExists(Paths.get(emptyFile.toString()));
    }
}