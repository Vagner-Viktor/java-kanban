package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    Long getIdCont();

    List<Task> getHistory();

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Subtask> getEpicSubtasks(Long epicId);

    void deleteAllTypesTasks();

    boolean deleteTask(Long taskId);

    boolean deleteEpic(Long epicId);

    boolean deleteSubtask(Long subtaskId);

    void deleteAllTasks();

    void deleteAllEpic();

    void deleteAllSubtasks();

    Long addTask(Task newTask);

    Long addEpic(Epic newEpic);

    Long addSubtask(Subtask newSubtask, Long epicId);

    boolean updateSubtask(Subtask subtask);

    boolean updateEpic(Epic epic);

    boolean updateTask(Task task);

    Subtask getSubtask(Long subtaskId);

    Task getTask(Long taskId);

    Epic getEpic(Long epicId);

    Status setCurrentEpicStatus(Epic epic);
}
