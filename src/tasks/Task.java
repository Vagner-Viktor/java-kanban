package tasks;

import java.util.Objects;

public class Task {
    private String name;
    private TaskTypes type;
    private String description;
    private Long id;
    private Status status;


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                Objects.equals(id, task.id) &&
                status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {
        return "\n" + type + "{" +
                "\n id = " + id +
                "\n name = " + name +
                "\n status = " + status +
                "\n description = " + description +
                '}';
    }

    @Override
    public Task clone() {
        Task task = new Task(this.name, this.description);
        task.setId(this.id);
        task.setStatus(this.status);
        task.setType(this.type);
        return task;
    }
}
