package sprint4.tasks;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String description, int id, Status status, int epicId) {
        super(taskName, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "sprint4.tasks.Subtask{" +
                "epicId='" + epicId + '\'' +
                ", taskName='" + getTaskName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }

}
