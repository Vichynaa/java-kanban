package sprint.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    public Epic(int epicId, String taskName, String description) {
        super(epicId, taskName, description, Status.NEW);
    }

    public Epic(int epicId, String taskName, String description, Status status) {
        super(epicId, taskName, description, status);
    }

    public ArrayList<Integer> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public void setSubtask(Subtask subtask) {
        this.subtasks.add(subtask.getId());
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String csvString() {
        return String.format("%d,%s,%s,%s,%s", this.getId(), Type.EPIC, this.getTaskName(),
                this.getStatus(), this.getDescription());
    }

    @Override
    public String toString() {
        return "sprint4.models.Epic{" +
                "subtasks=" + subtasks +
                ", status=" + getStatus() +
                ", taskName='" + getTaskName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }

}

