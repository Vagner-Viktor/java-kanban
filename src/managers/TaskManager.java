package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private Long idCont = 1L;
    private HashMap<Long, Task> tasksMap = new HashMap<>();
    private HashMap<Long, Epic> epicsMap = new HashMap<>();
    private HashMap<Long, Subtask> subtasksMap = new HashMap<>();


    public HashMap<Long, Task> getTasksMap() {
        return tasksMap;
    }

    public HashMap<Long, Epic> getEpicsMap() {
        return epicsMap;
    }

    public HashMap<Long, Subtask> getSubtasksMap() {
        return subtasksMap;
    }


    public ArrayList<Task> getAllTypesTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task value : tasksMap.values()) {
            Task newTask = new Task(value.getName(), value.getDescription());
            newTask.setId(value.getId());
            newTask.setType(TaskTypes.TASK);
            newTask.setStatus(value.getStatus());
            allTasks.add(newTask);
        }
        for (Epic value : epicsMap.values()) {
            Task newTask = new Task(value.getName(), value.getDescription());
            newTask.setId(value.getId());
            newTask.setType(TaskTypes.EPIC);
            newTask.setStatus(value.getStatus());
            allTasks.add(newTask);
            for (Long subtaskId : value.getSubtaskList()) {
                Subtask subtaskValue = subtasksMap.get(subtaskId);
                Task newSubtask = new Task(subtaskValue.getName(), subtaskValue.getDescription());
                newSubtask.setId(subtaskValue.getId());
                newSubtask.setType(TaskTypes.SUBTASK);
                newSubtask.setStatus(subtaskValue.getStatus());
                allTasks.add(newSubtask);
            }
        }
        return allTasks;
    }

    public ArrayList<Subtask> getAllEpicsSubtask(Long epicId) {
        if (!epicsMap.containsKey(epicId)) return null;
        ArrayList<Subtask> epicsSubtasksList = new ArrayList<>();
        for (Long subtaskId : epicsMap.get(epicId).getSubtaskList()) {
            epicsSubtasksList.add(subtasksMap.get(subtaskId));
        }
        return epicsSubtasksList;
    }

    public void deleteAllTypesTasks() {
        tasksMap.clear();
        epicsMap.clear();
        subtasksMap.clear();
    }

    public boolean deleteTask(Long taskId) {
        if (!tasksMap.containsKey(taskId)) return false;
        tasksMap.remove(taskId);
        return true;
    }

    public boolean deleteEpic(Long epicId) {
        if (!epicsMap.containsKey(epicId)) return false;
        for (Long subtaskId : epicsMap.get(epicId).getSubtaskList()) {
            if (subtasksMap.containsKey(subtaskId)) subtasksMap.remove(subtaskId);
        }
        epicsMap.remove(epicId);
        return true;
    }

    public boolean deleteSubtask(Long subtaskId) {
        if (!subtasksMap.containsKey(subtaskId)) return false;
        Long epicId = subtasksMap.get(subtaskId).getEpicId();
        if (!epicsMap.containsKey(epicId)) return false;
        epicsMap.get(epicId).removeSubtask(subtaskId);
        epicsMap.get(epicId).setStatus(setCurrentEpicStatus(epicsMap.get(epicId)));
        subtasksMap.remove(subtaskId);
        return true;
    }

    public void deleteAllTasks() {
        tasksMap.clear();
    }

    public void deleteAllEpic() {
        epicsMap.clear();
        subtasksMap.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epicsMap.values()) {
            epic.clearSubtaskList();
            epic.setStatus(Status.NEW);
        }
        subtasksMap.clear();
    }

    public Long addTask(Task newTask) {
        newTask.setId(idCont++);
        tasksMap.put(newTask.getId(), newTask);
        return newTask.getId();
    }

    public Long addEpic(Epic newEpic) {
        newEpic.setId(idCont++);
        epicsMap.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    public Long addSubtask(Subtask newSubtask, Long epicId) {
        if (!epicsMap.containsKey(epicId)) return null;
        newSubtask.setId(idCont++);
        newSubtask.setEpicId(epicId);
        subtasksMap.put(newSubtask.getId(), newSubtask);
        epicsMap.get(epicId).addSubtask(newSubtask.getId());
        epicsMap.get(epicId).setStatus(setCurrentEpicStatus(epicsMap.get(epicId)));
        return newSubtask.getId();
    }

    public boolean updateSubtask(Subtask subtask) {
        if (!subtasksMap.containsKey(subtask.getId())) return false;
        subtasksMap.put(subtask.getId(), subtask);
        if (epicsMap.containsKey(subtask.getEpicId()))
            epicsMap.get(subtask.getEpicId()).setStatus(setCurrentEpicStatus(epicsMap.get(subtask.getEpicId())));
        return true;
    }

    public boolean updateEpic(Epic epic) {
        if (!epicsMap.containsKey(epic.getId())) return false;
        epic.setStatus(setCurrentEpicStatus(epic));
        epicsMap.put(epic.getId(), epic);
        return true;
    }

    public boolean updateTask(Task task) {
        if (!tasksMap.containsKey(task.getId())) return false;
        tasksMap.put(task.getId(), task);
        return true;
    }


    public Subtask getSubtask(Long subtaskId) {
        return subtasksMap.get(subtaskId);
    }

    public Task getTask(Long taskId) {
        if (tasksMap.containsKey(taskId)) return tasksMap.get(taskId);
        return null;
    }

    public Epic getEpic(Long epicId) {
        if (epicsMap.containsKey(epicId)) return epicsMap.get(epicId);
        return null;
    }

    public Status setCurrentEpicStatus(Epic epic) {
        boolean allInDone = true;
        for (Long l : epic.getSubtaskList()) {
            if (!Status.DONE.equals(subtasksMap.get(l).getStatus())){
                allInDone = false;
                break;
            }
        }
        if (allInDone) return Status.DONE;

        for (Long l : epic.getSubtaskList()) {
            if (!Status.NEW.equals(subtasksMap.get(l).getStatus())) return Status.IN_PROGRESS;
        }
        return Status.NEW;
    }
}
