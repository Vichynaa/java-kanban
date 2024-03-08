package sprint.models;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String taskName, String description) {
        super(taskName, description);
        this.epicId = epicId;
    }

    public Subtask(int epicId, int id, String taskName, String description, Status status) {
        super(id, taskName, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "sprint4.models.Subtask{" +
                "epicId='" + epicId + '\'' +
                ", taskName='" + getTaskName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }

}
