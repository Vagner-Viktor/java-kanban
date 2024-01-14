import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Long newEpicId;
        Long newTaskId;
        Long newSubtaskId;
        Task newTask;
        Subtask newSubtask;
        Epic newEpic;

        newTask = new Task("Вынести мусор", "Вынести весь мусор, отсортировав его");
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
                "Договориться о хранении летних шин, выяснить стоимость и сроки хранения");
        taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = new Subtask("Провести диагностику шин",
                "Проверить давление, провести балансировку");
        taskManager.addSubtask(newSubtask, newEpicId);

        newEpic = new Epic("Путешествие на выходные",
                "Запланировать путешествие на выходные на машине");
        newEpicId = taskManager.addEpic(newEpic);
        newSubtask = new Subtask("Выбрать город",
                "Выбрать город в пределах 300км, где еще не были");
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
                "Определить список ресторанов, которые посетим по маршруту посещения достопримечательностей");
        taskManager.addSubtask(newSubtask, newEpicId);


        newEpic = new Epic("Перевести слова на английский",
                "Перевести и выучить слова на английском");
        newEpicId = taskManager.addEpic(newEpic);
        newSubtask = new Subtask("Задача",
                "Задача - tasks.Task");
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(newSubtask);
        newSubtask = new Subtask("Подзадача",
                "Подзадача - tasks.Subtask");
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(newSubtask);
        newSubtask = new Subtask("Эпик",
                "Эпик - tasks.Epic");
        newSubtaskId = taskManager.addSubtask(newSubtask, newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(newSubtask);

        System.out.println("\n\n\n" + taskManager.getTasks() +
                taskManager.getEpics() +
                taskManager.getSubtasksMap());


        newEpic = taskManager.getEpic(newEpicId);
        newEpic.setDescription("Только перевести слова с английского");
        taskManager.updateEpic(newEpic);
        newTask = taskManager.getTask(newTaskId);
        newTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(newTask);
        System.out.println("\n\n\n" + taskManager.getTasks() +
                taskManager.getEpics() +
                taskManager.getSubtasksMap());

        taskManager.deleteEpic(newEpicId);
        System.out.println("\n\n\n" + taskManager.getTasks() +
                taskManager.getEpics() +
                taskManager.getSubtasksMap());

        taskManager.deleteAllTasks();
        System.out.println("\n\n\n" + taskManager.getTasks() +
                taskManager.getEpics() +
                taskManager.getSubtasksMap());

        taskManager.deleteAllSubtasks();
        System.out.println("\n\n\n" + taskManager.getTasks() +
                taskManager.getEpics() +
                taskManager.getSubtasksMap());

        taskManager.deleteAllEpic();
        System.out.println("\n\n\n" + taskManager.getTasks() +
                taskManager.getEpics() +
                taskManager.getSubtasksMap());    }
}
