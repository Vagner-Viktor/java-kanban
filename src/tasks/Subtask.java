package tasks;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private Long epicId;

    public Subtask(String name, String description) {
        super(name, description);
        this.setType(TaskTypes.SUBTASK);
    }

    public Subtask(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.setType(TaskTypes.SUBTASK);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        String startTimeS = "null";
        String durationS = "null";
        if (this.getStartTime() != null && this.getEndTime() != null && this.getDuration() != null) {
            startTimeS = this.getStartTime().format(DATE_TIME_FORMATTER);
            durationS = String.format("%d:%02d", this.getDuration().toHours(), this.getDuration().toMinutesPart());
        }
        return "\n" + this.getType() + "{" +
                "\n id = " + this.getId() +
                "\n name = " + this.getName() +
                "\n status = " + this.getStatus() +
                "\n description = " + this.getDescription() +
                "\n startTime = " + startTimeS +
                "\n duration = " + durationS +
                "\n epicId=" + epicId +
                '}';
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        if (this.getId() != epicId) this.epicId = epicId;
    }
}
