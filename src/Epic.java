import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Long> subtaskList;

    public Epic(String name, String description, Long id) {
        super(name, description, id);
        this.subtaskList = new ArrayList<>();
        this.type = TaskTypes.EPIC;
    }

}
