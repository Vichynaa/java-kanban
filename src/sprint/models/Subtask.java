package sprint.models;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String taskName, String description, Integer duration, LocalDateTime startTime) {
        super(taskName, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(int epicId, int id, String taskName, String description, Status status, Integer duration, LocalDateTime startTime) {
        super(id, taskName, description, status, duration, startTime);
        this.epicId = epicId;
    }

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

    public String csvString() {
        if (getStartTime() == null) {
            return String.format("%d,%s,%s,%s,%s,%d", this.getId(), Type.SUBTASK, this.getTaskName(),
                    this.getStatus(), this.getDescription(),this.getEpicId());
        }
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d", this.getId(), Type.SUBTASK, this.getTaskName(),
                this.getStatus(), this.getDescription(), getDuration().toString(), getStartTime().toString(),this.getEpicId());
    }

    @Override
    public String toString() {
        return "sprint4.models.Subtask{" +
                "epicId='" + epicId + '\'' +
                ", taskName='" + getTaskName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }

}
