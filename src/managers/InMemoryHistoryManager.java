package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> tasksHistory = new ArrayList<>();
    private static final int HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        if (tasksHistory.size() >= HISTORY_SIZE) tasksHistory.remove(0);
        tasksHistory.add(task.clone());
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }
}
