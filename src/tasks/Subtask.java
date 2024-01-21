package tasks;


import java.util.Objects;

public class Subtask extends Task {
    private Long epicId;

    public Subtask(String name, String description) {
        super(name, description);
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
        return "\n" + this.getType() + "{" +
                "\n id = " + this.getId() +
                "\n name = " + this.getName() +
                "\n status = " + this.getStatus() +
                "\n description = " + this.getDescription() +
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
