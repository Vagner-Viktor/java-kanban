package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import utils.PrioritizedTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private Long idCont = 1L;
    private final Map<Long, Task> tasksMap = new HashMap<>();
    private final Map<Long, Epic> epicsMap = new HashMap<>();
    private final Map<Long, Subtask> subtasksMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final PrioritizedTasks prioritizedTasks = new PrioritizedTasks();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void setIdCont(Long idCont) {
        if (this.idCont < idCont) this.idCont = idCont;
    }

    @Override
    public Long getIdCont() {
        return idCont;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(Long epicId) {
        if (!epicsMap.containsKey(epicId)) return null;
        return epicsMap.get(epicId).getSubtaskList().stream()
                .map(subtaskId -> subtasksMap.get(subtaskId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllTypesTasks() {
        deleteAllTasks();
        deleteAllEpic();
        deleteAllTasks();
    }

    @Override
    public boolean deleteTask(Long taskId) {
        if (!tasksMap.containsKey(taskId)) return false;
        historyManager.remove(taskId);
        prioritizedTasks.remove(tasksMap.get(taskId));
        tasksMap.remove(taskId);
        return true;
    }

    @Override
    public boolean deleteEpic(Long epicId) {
        if (!epicsMap.containsKey(epicId)) return false;
        epicsMap.get(epicId).getSubtaskList().stream()
                .filter(subtaskId -> subtasksMap.containsKey(subtaskId))
                .forEach(subtaskId -> {
                    historyManager.remove(subtaskId);
                    prioritizedTasks.remove(subtasksMap.get(subtaskId));
                    subtasksMap.remove(subtaskId);
                });
        historyManager.remove(epicId);
        epicsMap.remove(epicId);
        return true;
    }

    @Override
    public boolean deleteSubtask(Long subtaskId) {
        if (!subtasksMap.containsKey(subtaskId)) return false;
        Long epicId = subtasksMap.get(subtaskId).getEpicId();
        if (!epicsMap.containsKey(epicId)) return false;
        epicsMap.get(epicId).removeSubtask(subtaskId);
        updateDataInEpic(epicsMap.get(epicId));
        historyManager.remove(subtaskId);
        prioritizedTasks.remove(subtasksMap.get(subtaskId));
        subtasksMap.remove(subtaskId);
        return true;
    }

    @Override
    public void deleteAllTasks() {
        clearHistorySet(tasksMap.keySet());
        prioritizedTasks.remove(getTasks());
        tasksMap.clear();
    }

    @Override
    public void deleteAllEpic() {
        clearHistorySet(epicsMap.keySet());
        epicsMap.clear();
        clearHistorySet(subtasksMap.keySet());
        prioritizedTasks.remove(new ArrayList<Task>(getSubtasks()));
        subtasksMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        epicsMap.values().stream()
                .forEach(epic -> {
                    epic.clearSubtaskList();
                    epic.setStatus(Status.NEW);
                });
        clearHistorySet(subtasksMap.keySet());
        prioritizedTasks.remove(new ArrayList<Task>(getSubtasks()));
        subtasksMap.clear();
    }

    @Override
    public void clearHistorySet(Set<Long> set) {
        set.stream()
                .forEach(l -> historyManager.remove(l));
    }

    @Override
    public Long addTask(Task newTask) {
        newTask.setId(getNewId(newTask.getId()));
        prioritizedTasks.add(newTask);
        tasksMap.put(newTask.getId(), newTask);
        return newTask.getId();
    }

    @Override
    public Long addEpic(Epic newEpic) {
        newEpic.setId(getNewId(newEpic.getId()));
        epicsMap.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    @Override
    public Long addSubtask(Subtask newSubtask, Long epicId) {
        if ((!epicsMap.containsKey(epicId)) || newSubtask.getId() == epicId) return null;
        newSubtask.setId(getNewId(newSubtask.getId()));
        newSubtask.setEpicId(epicId);
        subtasksMap.put(newSubtask.getId(), newSubtask);
        prioritizedTasks.add(newSubtask);
        epicsMap.get(epicId).addSubtask(newSubtask.getId());
        updateDataInEpic(epicsMap.get(epicId));
        return newSubtask.getId();
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (!subtasksMap.containsKey(subtask.getId()) ||
                !epicsMap.containsKey(subtask.getEpicId()) ||
                (subtask.getId() == subtask.getEpicId())) return false;
        prioritizedTasks.update(subtasksMap.get(subtask.getId()), subtask);
        subtasksMap.put(subtask.getId(), subtask);
        if (epicsMap.containsKey(subtask.getEpicId()))
            updateDataInEpic(epicsMap.get(subtask.getEpicId()));
        return true;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (!epicsMap.containsKey(epic.getId())) return false;
        updateDataInEpic(epic);
        epicsMap.put(epic.getId(), epic);
        return true;
    }

    @Override
    public boolean updateTask(Task task) {
        if (!tasksMap.containsKey(task.getId())) return false;
        prioritizedTasks.update(tasksMap.get(task.getId()), task);
        tasksMap.put(task.getId(), task);
        return true;
    }


    @Override
    public Subtask getSubtask(Long subtaskId) {
        Subtask subtask = subtasksMap.get(subtaskId);
        saveInHistory(subtasksMap.get(subtaskId));
        return subtask;
    }

    @Override
    public Task getTask(Long taskId) {
        Task task = tasksMap.get(taskId);
        saveInHistory(tasksMap.get(taskId));
        return task;
    }

    @Override
    public Epic getEpic(Long epicId) {
        Epic epic = epicsMap.get(epicId);
        saveInHistory(epicsMap.get(epicId));
        return epic;
    }

    @Override
    public void updateDataInEpic(Epic epic) {
        epic.setStatus(setCurrentEpicStatus(epic));
        updateEpicDurationAndStartEndTime(epic);
    }

    @Override
    public Status setCurrentEpicStatus(Epic epic) {
        if (epic.getSubtaskList().stream()
                .filter(l -> !Status.DONE.equals(subtasksMap.get(l).getStatus()))
                .findFirst()
                .isEmpty()) return Status.DONE;
        if (epic.getSubtaskList().stream()
                .filter(l -> !Status.NEW.equals(subtasksMap.get(l).getStatus()))
                .findFirst()
                .isEmpty()) return Status.NEW;
        return Status.IN_PROGRESS;
    }

    @Override
    public void updateEpicDurationAndStartEndTime(Epic epic) {
        if (epic.getSubtaskList().isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(null);
            return;
        }
        LocalDateTime startTime = epic.getSubtaskList().stream()
                .map(subtaskId -> subtasksMap.get(subtaskId).getStartTime())
                .min(Comparator.naturalOrder())
                .orElseThrow();
        LocalDateTime endTime = epic.getSubtaskList().stream()
                .map(subtaskId -> subtasksMap.get(subtaskId).getStartTime().plus(subtasksMap.get(subtaskId).getDuration()))
                .max(Comparator.naturalOrder())
                .orElseThrow();
        Duration duration = Duration.between(startTime, endTime);
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    @Override
    public void saveInHistory(Task task) {
        if (task != null) historyManager.add(task);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks.getPrioritizedTasks();
    }

    @Override
    public boolean checkTheIntersectionOfTasks(Task task) {
        return prioritizedTasks.checkTheIntersectionOfTasks(task);
    }

    private Long getNewId(Long currentId) {
        Long newID;
        if (currentId != null) {
            newID = currentId;
        } else newID = idCont;
        while (true) {
            if (epicsMap.containsKey(newID)
                    || tasksMap.containsKey(newID)
                    || subtasksMap.containsKey(newID)) {
                newID = idCont++;
            } else break;
        }
        return newID;
    }
}

