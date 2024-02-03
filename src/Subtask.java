import java.util.HashMap;

public class Subtask extends Task {
    Epic epic;

    public Subtask(String taskName, String description, Epic epic) {
        super(taskName, description);
        this.epic = epic;
    }

    public Subtask(String taskName, String description, int id, Status status, Epic epic) {
        super(taskName, description, id, status);
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
