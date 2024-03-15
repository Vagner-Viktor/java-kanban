package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Long> subtasksList;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksList = new ArrayList<>();
        this.setType(TaskTypes.EPIC);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksList, epic.subtasksList) &&
                Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksList, endTime);
    }

    @Override
    public String toString() {
        return "\n" + this.getType() + "{" +
                "\n id = " + this.getId() +
                "\n name = " + this.getName() +
                "\n status = " + this.getStatus() +
                "\n description = " + this.getDescription() +
                "\n startTime = " + this.getStartTime().format(DATE_TIME_FORMATTER) +
                "\n endTime = " + this.getEndTime().format(DATE_TIME_FORMATTER) +
                "\n duration = " + String.format("%d:%02d", this.getDuration().toHours(), this.getDuration().toMinutesPart()) +
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

    public void removeSubtask(Long subtaskId) {
        subtasksList.remove(subtaskId);
    }

    public void addSubtask(Long subtaskId) {
        if ((subtaskId != this.getId()) && !subtasksList.contains(subtaskId)) subtasksList.add(subtaskId);
    }
}
