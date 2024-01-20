package managers;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> tasksHistory = new ArrayList<>();
    private int historySize;

    InMemoryHistoryManager(int historySize) {
        this.historySize = historySize;
    }

    @Override
    public void add(Task task) {
        if (tasksHistory.size() >= historySize) tasksHistory.remove(0);
        tasksHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return tasksHistory;
    }
}
