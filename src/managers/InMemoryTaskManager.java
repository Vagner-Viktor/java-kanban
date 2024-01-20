package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private Long idCont = 1L;
    private HashMap<Long, Task> tasksMap = new HashMap<>();
    private HashMap<Long, Epic> epicsMap = new HashMap<>();
    private HashMap<Long, Subtask> subtasksMap = new HashMap<>();

    final private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Long getIdCont() {
        return idCont;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(Long epicId) {
        if (!epicsMap.containsKey(epicId)) return null;
        ArrayList<Subtask> epicsSubtasksList = new ArrayList<>();
        for (Long subtaskId : epicsMap.get(epicId).getSubtaskList()) {
            epicsSubtasksList.add(subtasksMap.get(subtaskId));
        }
        return epicsSubtasksList;
    }

    @Override
    public void deleteAllTypesTasks() {
        tasksMap.clear();
        epicsMap.clear();
        subtasksMap.clear();
    }

    @Override
    public boolean deleteTask(Long taskId) {
        if (!tasksMap.containsKey(taskId)) return false;
        tasksMap.remove(taskId);
        return true;
    }

    @Override
    public boolean deleteEpic(Long epicId) {
        if (!epicsMap.containsKey(epicId)) return false;
        for (Long subtaskId : epicsMap.get(epicId).getSubtaskList()) {
            if (subtasksMap.containsKey(subtaskId)) subtasksMap.remove(subtaskId);
        }
        epicsMap.remove(epicId);
        return true;
    }

    @Override
    public boolean deleteSubtask(Long subtaskId) {
        if (!subtasksMap.containsKey(subtaskId)) return false;
        Long epicId = subtasksMap.get(subtaskId).getEpicId();
        if (!epicsMap.containsKey(epicId)) return false;
        epicsMap.get(epicId).removeSubtask(subtaskId);
        epicsMap.get(epicId).setStatus(setCurrentEpicStatus(epicsMap.get(epicId)));
        subtasksMap.remove(subtaskId);
        return true;
    }

    @Override
    public void deleteAllTasks() {
        tasksMap.clear();
    }

    @Override
    public void deleteAllEpic() {
        epicsMap.clear();
        subtasksMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epicsMap.values()) {
            epic.clearSubtaskList();
            epic.setStatus(Status.NEW);
        }
        subtasksMap.clear();
    }

    @Override
    public Long addTask(Task newTask) {
        newTask.setId(idCont++);
        tasksMap.put(newTask.getId(), newTask);
        return newTask.getId();
    }

    @Override
    public Long addEpic(Epic newEpic) {
        newEpic.setId(idCont++);
        epicsMap.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    @Override
    public Long addSubtask(Subtask newSubtask, Long epicId) {
        if ((!epicsMap.containsKey(epicId)) || newSubtask.getId() == epicId) return null;
        newSubtask.setId(idCont++);
        newSubtask.setEpicId(epicId);
        subtasksMap.put(newSubtask.getId(), newSubtask);
        epicsMap.get(epicId).addSubtask(newSubtask.getId());
        epicsMap.get(epicId).setStatus(setCurrentEpicStatus(epicsMap.get(epicId)));
        return newSubtask.getId();
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (!subtasksMap.containsKey(subtask.getId()) ||
                !epicsMap.containsKey(subtask.getEpicId()) ||
                (subtask.getId() == subtask.getEpicId())) return false;
        subtasksMap.put(subtask.getId(), subtask);
        if (epicsMap.containsKey(subtask.getEpicId()))
            epicsMap.get(subtask.getEpicId()).setStatus(setCurrentEpicStatus(epicsMap.get(subtask.getEpicId())));
        return true;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (!epicsMap.containsKey(epic.getId())) return false;
        epic.setStatus(setCurrentEpicStatus(epic));
        epicsMap.put(epic.getId(), epic);
        return true;
    }

    @Override
    public boolean updateTask(Task task) {
        if (!tasksMap.containsKey(task.getId())) return false;
        tasksMap.put(task.getId(), task);
        return true;
    }


    @Override
    public Subtask getSubtask(Long subtaskId) {
        if (!subtasksMap.containsKey(subtaskId)) return null;
        historyManager.add(subtasksMap.get(subtaskId));
        return subtasksMap.get(subtaskId);
    }

    @Override
    public Task getTask(Long taskId) {
        if (!tasksMap.containsKey(taskId)) return null;
        historyManager.add(tasksMap.get(taskId));
        return tasksMap.get(taskId);
    }

    @Override
    public Epic getEpic(Long epicId) {
        if (!epicsMap.containsKey(epicId)) return null;
        historyManager.add(epicsMap.get(epicId));
        return epicsMap.get(epicId);
    }

    @Override
    public Status setCurrentEpicStatus(Epic epic) {
        boolean allInDone = true;
        for (Long l : epic.getSubtaskList()) {
            if (!Status.DONE.equals(subtasksMap.get(l).getStatus())) {
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

