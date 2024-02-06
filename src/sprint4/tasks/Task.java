package sprint4.tasks;

import java.util.Objects;

public class Task {
    private final String taskName;
    private final String description;
    private final Integer id;
    private Status status;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.id = TaskManager.createId();
        this.status = Status.NEW;
    }

    public Task(String taskName, String description, int id, Status status) {
        this.taskName = taskName;
        this.description = description;
        this.id = id;
        this.status = status;
    }


    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
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
        return "sprint4.tasks.Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
