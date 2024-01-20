package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    Long getIdCont();

    ArrayList<Task> getHistory();

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Subtask> getEpicSubtasks(Long epicId);

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
