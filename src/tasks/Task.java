package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private String name;
    private TaskTypes type;
    private String description;
    private Long id;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.type = TaskTypes.TASK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskTypes getType() {
        return type;
    }

    public void setType(TaskTypes type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                type == task.type &&
                Objects.equals(description, task.description) &&
                Objects.equals(id, task.id) &&
                status == task.status &&
                Objects.equals(duration, task.duration) &&
                Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, description, id, status, duration, startTime);
    }

    @Override
    public String toString() {
        return "\n" + type + "{" +
                "\n id = " + id +
                "\n name = " + name +
                "\n status = " + status +
                "\n description = " + description +
                "\n startTime = " + startTime.format(DATE_TIME_FORMATTER) +
                "\n duration = " + String.format("%d:%02d", duration.toHours(), duration.toMinutesPart()) +
                '}';
    }

    @Override
    public Task clone() {
        Task task = new Task(this.name, this.description);
        task.setId(this.id);
        task.setStatus(this.status);
        task.setType(this.type);
        task.setStartTime(this.startTime);
        task.setDuration(this.duration);
        return task;
    }
}
