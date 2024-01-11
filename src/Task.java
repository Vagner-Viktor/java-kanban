import java.util.Objects;

public class Task {
    String name;
    TaskTypes type;
    String description;
    Long id;
    Status status;

    public Task(String name, String description, Long id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
        this.type = TaskTypes.TASK;
    }

    public Task(String name, String description, Long id, TaskTypes type, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = type;
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
}
