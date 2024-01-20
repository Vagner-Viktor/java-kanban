package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Long> subtasksList;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksList = new ArrayList<>();
        this.setType(TaskTypes.EPIC);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksList, epic.subtasksList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksList);
    }

    @Override
    public String toString() {
        return this.getType() + "{" +
                "\n id = " + this.getId() +
                "\n name = " + this.getName() +
                "\n status = " + this.getStatus() +
                "\n description = " + this.getDescription() +
                "\n subtasksList=" + subtasksList +
                '}';
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
