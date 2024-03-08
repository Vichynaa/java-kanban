package sprint.models;

import sprint.managers.InMemoryTaskManager;

import java.util.Objects;

public class Task {
    private final Integer id;
    private String taskName;
    private String description;
    private Status status;

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

    @Override
    public String toString() {
        return "sprint4.models.Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
