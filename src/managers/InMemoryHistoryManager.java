package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Long, Node> tasksHistoryMap = new HashMap<>();
    private Node<Task> head = null;
    private Node<Task> tail = null;

    @Override
    public void add(Task task) {
        remove(task.getId());
        linkLast(task.clone());
    }

    public void remove(Long id) {
        removeNode(tasksHistoryMap.get(id));
        tasksHistoryMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        tasksHistoryMap.put(newNode.data.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> curentNode = head;
        while (curentNode != null) {
            tasks.add(curentNode.data);
            curentNode = curentNode.next;
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (node == null) return;
        Node<Task> prevNode = node.prev;
        Node<Task> nextNode = node.next;
        if (prevNode == null)
            head = nextNode;
        else
            prevNode.next = nextNode;
        if (nextNode == null)
            tail = prevNode;
        else
            nextNode.prev = prevNode;
    }
}
