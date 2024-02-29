import managers.FileBackedTaskManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        taskManager = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));
        //   makeExampleData(taskManager);
        printAllTasks(taskManager);
    }

    private static void makeExampleData(TaskManager taskManager) {
        Long newEpicId;
        Long newTaskId;
        Long newSubtaskId;
        Task newTask;
        Subtask newSubtask;
        Epic newEpic;

        newTask = new Task("Вынести мусор", "Вынести весь мусор после сортировки");
        newTaskId = taskManager.addTask(newTask);

        newEpic = new Epic("Поменять зимние шины на летние на автомобиле",
                "Поменять шины в автомобиле с летних на зимние");
        newEpicId = taskManager.addEpic(newEpic);
        newSubtask = new Subtask("Заказать зимние шины",
                "Позвонить в фирму по хранению шин и заказать со склада зимние шины");
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Назначить встречу по замене шин",
                "Договориться о времени замены шин");
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Договориться о хранении летних шин",
                "Договориться о хранении летних шин и выяснить стоимость и сроки хранения");
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Провести диагностику шин",
                "Проверить давление и провести балансировку");
        taskManager.addSubtask(newSubtask, newEpicId);

        newEpic = new Epic("Путешествие на выходные",
                "Запланировать путешествие на выходные на машине");
        newEpicId = taskManager.addEpic(newEpic);
        newSubtask = new Subtask("Выбрать город",
                "Выбрать город в пределах 300км. где еще не были");
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Выбрать отель",
                "Выбрать отель в центре с парковкой");
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(newSubtask);

        newSubtask = new Subtask("Составить список достопримечательностей",
                "Найти главные достопримечательности города и составить маршрут посещения");
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Выбрать рестораны",
                "Определить список ресторанов по маршруту посещения достопримечательностей");
        taskManager.addSubtask(newSubtask, newEpicId);


        newEpic = new Epic("Перевести слова на английский",
                "Перевести и выучить слова на английском");
        newEpicId = taskManager.addEpic(newEpic);
        newSubtask = new Subtask("Задача",
                "Задача - Task");
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(newSubtask);
        newSubtask = new Subtask("Подзадача",
                "Подзадача - Subtask");
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(newSubtask);
        newSubtask = new Subtask("Эпик",
                "Эпик - Epic");
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(newSubtask);

        printAllTasks(taskManager);

        newEpic = taskManager.getEpic(newEpicId);
        newEpic.setDescription("Только перевести слова с английского");
        taskManager.updateEpic(newEpic);
        newTask = taskManager.getTask(newTaskId);
        newTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(newTask);
        printAllTasks(taskManager);

        taskManager.deleteEpic(newEpicId);
        printAllTasks(taskManager);
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
