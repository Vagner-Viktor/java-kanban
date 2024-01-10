public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Long newEpicId;
        Long newTaskId;
        Long newSubtaskId;
        Task newTask;
        Subtask newSubtask;
        Epic newEpic;

        newTaskId = taskManager.addTask("Вынести мусор", "Вынести весь мусор, отсортировав его");

        newEpicId = taskManager.addEpic("Поменять зимние шины на летние на автомобиле",
                "Поменять шины в автомобиле с летних на зимние");
        taskManager.addSubtask("Заказать зимние шины",
                "Позвонить в фирму по хранению шин и заказать со склада зимние шины",
                newEpicId);
        taskManager.addSubtask("Назначить встречу по замене шин",
                "Договориться о времени замены шин",
                newEpicId);
        taskManager.addSubtask("Договориться о хранении летних шин",
                "Договориться о хранении летних шин, выяснить стоимость и сроки хранения",
                newEpicId);
        taskManager.addSubtask("Провести диагностику шин",
                "Проверить давление, провести сход-развал",
                newEpicId);

        newEpicId = taskManager.addEpic("Путешествие на выходные",
                "Запланировать путешествие на выходные на машине");
        taskManager.addSubtask("Выбрать город",
                "Выбрать город в пределах 300км, где еще не были",
                newEpicId);
        newSubtaskId = taskManager.addSubtask("Выбрать отель",
                "Выбрать отель в центре с парковкой",
                newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.status = Status.IN_PROGRESS;
        taskManager.updateSubtask(newSubtask);

        taskManager.addSubtask("Составить список достопримечательностей",
                "Найти главные достопримечательности города и составить маршрут посещения",
                newEpicId);
        taskManager.addSubtask("Выбрать рестораны",
                "Определить список ресторанов, которые посетим по маршруту посещения достопримечательностей",
                newEpicId);


        newEpicId = taskManager.addEpic("Перевести слова на английский",
                "Перевести и выучить слова на английском");
        newSubtaskId = taskManager.addSubtask("Задача",
                "Задача - Task",
                newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.status = Status.DONE;
        taskManager.updateSubtask(newSubtask);
        newSubtaskId = taskManager.addSubtask("Подзадача",
                "Подзадача - Subtask",
                newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.status = Status.DONE;
        taskManager.updateSubtask(newSubtask);
        newSubtaskId = taskManager.addSubtask("Эпик",
                "Эпик - Epic",
                newEpicId);
        newSubtask = taskManager.getSubtask(newSubtaskId);
        newSubtask.status = Status.DONE;
        taskManager.updateSubtask(newSubtask);

        System.out.println("\n\n\n" + taskManager.getAllTypesTasks());


        newEpic = taskManager.getEpic(newEpicId);
        newEpic.description = "Только перевести слова с английского";
        taskManager.updateEpic(newEpic);
        newTask = taskManager.getTask(newTaskId);
        newTask.status = Status.IN_PROGRESS;
        taskManager.updateTask(newTask);
        System.out.println("\n\n\n" + taskManager.getAllTypesTasks());


        taskManager.deleteEpic(newEpicId);
        System.out.println("\n\n\n" + taskManager.getAllTypesTasks());

        taskManager.deleteAllTasks();
        System.out.println("\n\n\n" + taskManager.getAllTypesTasks());

        taskManager.deleteAllSubtasks();
        System.out.println("\n\n\n" + taskManager.getAllTypesTasks());

        taskManager.deleteAllEpic();
        System.out.println("\n\n\n" + taskManager.getAllTypesTasks());
    }
}
