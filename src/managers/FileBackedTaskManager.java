package managers;

import exceptions.ManagerLoadEmptyException;
import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import tasks.*;
import utils.FormatterUtil;

import java.io.*;
import java.util.List;
import java.util.Set;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTaskManager() {
        super();
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public boolean deleteTask(Long taskId) {
        boolean result = super.deleteTask(taskId);
        save();
        return result;
    }

    @Override
    public boolean deleteEpic(Long epicId) {
        boolean result = super.deleteEpic(epicId);
        save();
        return result;
    }

    @Override
    public boolean deleteSubtask(Long subtaskId) {
        boolean result = super.deleteSubtask(subtaskId);
        save();
        return result;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void clearHistorySet(Set<Long> set) {
        super.clearHistorySet(set);
        save();
    }

    @Override
    public Long addTask(Task newTask) {
        Long result = super.addTask(newTask);
        save();
        return result;
    }

    @Override
    public Long addEpic(Epic newEpic) {
        Long result = super.addEpic(newEpic);
        save();
        return result;
    }

    @Override
    public Long addSubtask(Subtask newSubtask, Long epicId) {
        Long result = super.addSubtask(newSubtask, epicId);
        save();
        return result;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean result = super.updateSubtask(subtask);
        save();
        return result;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean result = super.updateEpic(epic);
        save();
        return result;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean result = super.updateTask(task);
        save();
        return result;
    }

    @Override
    public void saveInHistory(Task task) {
        super.saveInHistory(task);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        Long countIndex = 1L;
        try (BufferedReader fileRead = new BufferedReader(new FileReader(file))) {
            String line = fileRead.readLine();
            if (line == null) {
                throw new ManagerLoadEmptyException();
            } else if (!line.equals("id, type, name, status, description, epic")) {
                throw new ManagerLoadException();
            }
            while (fileRead.ready()) {
                line = fileRead.readLine();
                if (line.equals("History")) {
                    List<Long> history = FormatterUtil.historyFromString(fileRead.readLine());
                    for (Long l : history) {
                        if (fileBackedTaskManager.getTask(l) != null) {
                            fileBackedTaskManager.saveInHistory(fileBackedTaskManager.getTask(l));
                        }
                        if (fileBackedTaskManager.getEpic(l) != null) {
                            fileBackedTaskManager.saveInHistory(fileBackedTaskManager.getEpic(l));
                        }
                        if (fileBackedTaskManager.getSubtask(l) != null) {
                            fileBackedTaskManager.saveInHistory(fileBackedTaskManager.getSubtask(l));
                        }
                    }
                    break;
                }
                Task task = FormatterUtil.fromString(line);
                if (task == null) break;
                if (task.getId() > countIndex) countIndex = task.getId() + 1;
                switch (task.getType()) {
                    case TASK:
                        fileBackedTaskManager.addTask(task);
                        break;
                    case EPIC:
                        fileBackedTaskManager.addEpic((Epic) task);
                        break;
                    case SUBTASK:
                        fileBackedTaskManager.addSubtask((Subtask) task, ((Subtask) task).getEpicId());
                        break;
                }
            }
            fileBackedTaskManager.setIdCont(countIndex);
            fileBackedTaskManager.setFile(file);
        } catch (IOException e) {
            throw new ManagerLoadException();
        }
        return fileBackedTaskManager;
    }

    private void save() throws ManagerSaveException {
        if (file == null) return;
        try (BufferedWriter fileWrite = new BufferedWriter(new FileWriter(file))) {
            fileWrite.write("id, type, name, status, description, epic");
            fileWrite.newLine();
            for (Task task : getTasks()) {
                fileWrite.write(FormatterUtil.toString(task));
                FormatterUtil.fromString(FormatterUtil.toString(task));
                fileWrite.newLine();
            }
            for (Task epic : getEpics()) {
                fileWrite.write(FormatterUtil.toString(epic));
                fileWrite.newLine();
            }
            for (Task subtask : getSubtasks()) {
                fileWrite.write(FormatterUtil.toString(subtask));
                fileWrite.newLine();
            }
            fileWrite.write("History");
            fileWrite.newLine();
            fileWrite.write(FormatterUtil.historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }
}
