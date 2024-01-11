import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private Long idCont;
    private HashMap<Long, Task> tasksList;
    private HashMap<Long, Epic> epicsList;
    private HashMap<Long, Subtask> subtasksList;

    public HashMap<Long, Task> getTasksList() {
        return tasksList;
    }

    public HashMap<Long, Epic> getEpicsList() {
        return epicsList;
    }

    public HashMap<Long, Subtask> getSubtasksList() {
        return subtasksList;
    }

    public TaskManager() {
        this.tasksList = new HashMap<>();
        this.epicsList = new HashMap<>();
        this.subtasksList = new HashMap<>();
        this.idCont = 1L;
    }

    public ArrayList<Task> getAllTypesTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task value : tasksList.values()) {
            Task newTask = new Task(value.name, value.description, value.id, TaskTypes.TASK, value.status);
            allTasks.add(newTask);
        }
        for (Epic value : epicsList.values()) {
            Task newTask = new Task(value.name, value.description, value.id, TaskTypes.EPIC, value.status);
            allTasks.add(newTask);
            for (Long subtaskId : value.subtaskList) {
                Subtask subtaskValue = subtasksList.get(subtaskId);
                Task newSubtask = new Task(subtaskValue.name, subtaskValue.description, subtaskValue.id, TaskTypes.SUBTASK, subtaskValue.status);
                allTasks.add(newSubtask);
            }
        }
        return allTasks;
    }

    public ArrayList<Subtask> getAllEpicsSubtask(Long epicId) {
        if (!epicsList.containsKey(epicId)) return null;
        ArrayList<Subtask> epicsSubtasksList = new ArrayList<>();
        for (Long subtaskId : epicsList.get(epicId).subtaskList) {
            epicsSubtasksList.add(subtasksList.get(subtaskId));
        }
        return epicsSubtasksList;
    }

    public void deleteAllTypesTasks() {
        tasksList.clear();
        epicsList.clear();
        subtasksList.clear();
    }

    public boolean deleteTask(Long taskId) {
        if (!tasksList.containsKey(taskId)) return false;
        tasksList.remove(taskId);
        return true;
    }

    public boolean deleteEpic(Long epicId) {
        if (!epicsList.containsKey(epicId)) return false;
        for (Long subtaskId : epicsList.get(epicId).subtaskList) {
            if (subtasksList.containsKey(subtaskId)) subtasksList.remove(subtaskId);
        }
        epicsList.remove(epicId);
        return true;
    }

    public boolean deleteSubtask(Long subtaskId) {
        if (!subtasksList.containsKey(subtaskId)) return false;
        Long epicId = subtasksList.get(subtaskId).epicId;
        if (!epicsList.containsKey(epicId)) return false;
        epicsList.get(epicId).subtaskList.remove(subtaskId);
        epicsList.get(epicId).status = setCurrentEpicStatus(epicsList.get(epicId));
        subtasksList.remove(subtaskId);
        return true;
    }

    public void deleteAllTasks() {
        tasksList.clear();
    }

    public void deleteAllEpic() {
        epicsList.clear();
        subtasksList.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epicsList.values()) {
            epic.subtaskList.clear();
            epic.status = Status.NEW;
        }
        subtasksList.clear();
    }

    Long addTask(String name, String description) {
        Task task = new Task(name, description, idCont);
        tasksList.put(idCont, task);
        idCont++;
        return task.id;
    }

    Long addEpic(String name, String description) {
        Epic epic = new Epic(name, description, idCont);
        epicsList.put(idCont, epic);
        idCont++;
        return epic.id;
    }

    Long addSubtask(String name, String description, Long epicId) {
        if (!epicsList.containsKey(epicId)) return null;
        Subtask subtask = new Subtask(name, description, idCont, epicId);
        subtasksList.put(idCont, subtask);
        epicsList.get(epicId).subtaskList.add(idCont);
        epicsList.get(epicId).status = setCurrentEpicStatus(epicsList.get(epicId));
        idCont++;
        return subtask.id;
    }

    public boolean updateSubtask(Subtask subtask) {
        if (!subtasksList.containsKey(subtask.id)) return false;
        subtasksList.put(subtask.id, subtask);
        if (epicsList.containsKey(subtask.epicId))
            epicsList.get(subtask.epicId).status = setCurrentEpicStatus(epicsList.get(subtask.epicId));
        return true;
    }

    public boolean updateEpic(Epic epic) {
        if (!epicsList.containsKey(epic.id)) return false;
        epic.status = setCurrentEpicStatus(epic);
        epicsList.put(epic.id, epic);
        return true;
    }

    public boolean updateTask(Task task) {
        if (!tasksList.containsKey(task.id)) return false;
        tasksList.put(task.id, task);
        return true;
    }


    public Subtask getSubtask(Long subtaskId) {
        if (subtasksList.containsKey(subtaskId)) return subtasksList.get(subtaskId);
        return null;
    }

    public Task getTask(Long taskId) {
        if (tasksList.containsKey(taskId)) return tasksList.get(taskId);
        return null;
    }

    public Epic getEpic(Long epicId) {
        if (epicsList.containsKey(epicId)) return epicsList.get(epicId);
        return null;
    }

    public Status setCurrentEpicStatus(Epic epic) {
        boolean allInDone = true;
        for (Long l : epic.subtaskList) {
            if (!Status.DONE.equals(subtasksList.get(l).status)) {
                allInDone = false;
                break;
            }
        }
        if (allInDone) return Status.DONE;

        for (Long l : epic.subtaskList) {
            if (!Status.NEW.equals(subtasksList.get(l).status)) return Status.IN_PROGRESS;
        }
        return Status.NEW;
    }
}
