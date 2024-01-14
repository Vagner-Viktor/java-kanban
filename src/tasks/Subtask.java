package tasks;


public class Subtask extends Task {
    private Long epicId;

    public Subtask(String name, String description) {
        super(name, description);
        this.setType(TaskTypes.SUBTASK);
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }
}
