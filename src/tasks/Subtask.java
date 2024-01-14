package tasks;


public class Subtask extends Task {
    private Long epicId;

    public Subtask(String name, String description) {
        super(name, description);
        this.setType(TaskTypes.SUBTASK);
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
        this.epicId = epicId;
    }
}
