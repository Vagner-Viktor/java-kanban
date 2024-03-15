import managers.FileBackedTaskManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        //TaskManager taskManager = Managers.getDefault();
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));
        //taskManager = new FileBackedTaskManager(new File("tasks.csv"));
        //makeExampleData(taskManager);
        printAllTasks(taskManager);
        //System.out.println("\n\n\nSorted:" + taskManager.getPrioritizedTasks());
    }

    private static void makeExampleData(TaskManager taskManager) {
        Long newEpicId;
        Long newTaskId;
        Long newSubtaskId;
        Task newTask;
        Subtask newSubtask;
        Epic newEpic;

        newTask = new Task("Вынести мусор", "Вынести весь мусор после сортировки");
        newTask.setStartTime(LocalDateTime.of(2024,03,01,10,00));
        newTask.setDuration(Duration.ofMinutes(60));
        newTaskId = taskManager.addTask(newTask);

        newEpic = new Epic("Поменять зимние шины на летние на автомобиле",
                "Поменять шины в автомобиле с летних на зимние");
        newEpicId = taskManager.addEpic(newEpic);
        newSubtask = new Subtask("Заказать зимние шины",
                "Позвонить в фирму по хранению шин и заказать со склада зимние шины");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,02,10,00));
        newSubtask.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Назначить встречу по замене шин",
                "Договориться о времени замены шин");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,02,11,00));
        newSubtask.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Договориться о хранении летних шин",
                "Договориться о хранении летних шин и выяснить стоимость и сроки хранения");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,02,12,00));
        newSubtask.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Провести диагностику шин",
                "Проверить давление и провести балансировку");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,02,13,00));
        newSubtask.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(newSubtask, newEpicId);

        newEpic = new Epic("Путешествие на выходные",
                "Запланировать путешествие на выходные на машине");
        newEpicId = taskManager.addEpic(newEpic);
        newSubtask = new Subtask("Выбрать город",
                "Выбрать город в пределах 300км. где еще не были");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,03,10,00));
        newSubtask.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Выбрать отель",
                "Выбрать отель в центре с парковкой");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,03,11,00));
        newSubtask.setDuration(Duration.ofMinutes(60));
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(newSubtask);

        newSubtask = new Subtask("Составить список достопримечательностей",
                "Найти главные достопримечательности города и составить маршрут посещения");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,03,12,00));
        newSubtask.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Выбрать рестораны",
                "Определить список ресторанов по маршруту посещения достопримечательностей");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,03,13,00));
        newSubtask.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(newSubtask, newEpicId);


        newEpic = new Epic("Перевести слова на английский",
                "Перевести и выучить слова на английском");
        newEpicId = taskManager.addEpic(newEpic);
        newSubtask = new Subtask("Задача",
                "Задача - Task");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,04,10,00));
        newSubtask.setDuration(Duration.ofMinutes(30));
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(newSubtask);
        newSubtask = new Subtask("Подзадача",
                "Подзадача - Subtask");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,04,10,30));
        newSubtask.setDuration(Duration.ofMinutes(30));
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(newSubtask);
        newSubtask = new Subtask("Эпик",
                "Эпик - Epic");
        newSubtask.setStartTime(LocalDateTime.of(2024,03,04,11,00));
        newSubtask.setDuration(Duration.ofMinutes(30));
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setDuration(Duration.ofMinutes(60));
        newSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(newSubtask);

        printAllTasks(taskManager);

        newEpic = taskManager.getEpic(newEpicId);
        newEpic.setDescription("Только перевести слова с английского");
        taskManager.updateEpic(newEpic);
        newTask = taskManager.getTask(newTaskId);
        newTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(newTask);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История. Всего элементов: " + manager.getHistory().size());
        int index = 1;
        for (Task task : manager.getHistory()) {
            System.out.println(index++ + ". " + task);
        }
    }
}
