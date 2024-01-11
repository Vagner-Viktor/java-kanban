public class Subtask extends Task {
    Long epicId;

    public Subtask(String name, String description, Long id, Long epicId) {
        super(name, description, id);
        this.epicId = epicId;
        this.type = TaskTypes.SUBTASK;
    }
}
