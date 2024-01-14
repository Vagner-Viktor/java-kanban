package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Long> subtasksList;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksList = new ArrayList<>();
        this.setType(TaskTypes.EPIC);
    }

    public ArrayList<Long> getSubtaskList() {
        return subtasksList;
    }

    public void setSubtaskList(ArrayList<Long> subtaskList) {
        this.subtasksList = subtaskList;
    }

    public void clearSubtaskList() {
        subtasksList.clear();
    }

    public void removeSubtask(Long subtaskId){
        subtasksList.remove(subtaskId);
    }

    public void addSubtask(Long subtaskId){
        subtasksList.add(subtaskId);
    }
}
