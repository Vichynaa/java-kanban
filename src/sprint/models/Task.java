package sprint.models;

import sprint.managers.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final Integer id;
    private String taskName;
    private String description;
    private Status status;
    private Duration duration = null;
    private LocalDateTime startTime = null;

    public Task(String taskName, String description, Integer duration, LocalDateTime startTime) {
        this.id = InMemoryTaskManager.createId();
        this.taskName = taskName;
        this.description = description;
        this.status = Status.NEW;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public Task(int id, String taskName, String description, Status status, Integer duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public Task(String taskName, String description) {
        this.id = InMemoryTaskManager.createId();
        this.taskName = taskName;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(int id, String taskName, String description, Status status) {
        this.taskName = taskName;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String csvString() {
        if (startTime == null) {
            return String.format("%d,%s,%s,%s,%s", this.getId(), Type.TASK, this.getTaskName(),
                    this.getStatus(), this.getDescription());
        }
        return String.format("%d,%s,%s,%s,%s,%s,%s", this.getId(), Type.TASK, this.getTaskName(),
                this.getStatus(), this.getDescription(), this.duration.toString(), this.startTime.toString());
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
