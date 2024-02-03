import java.util.HashMap;
import java.util.Objects;

public class Task {
    protected String taskName;
    protected String description;
    protected int id;
    protected Status status;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.id = IdUtils.createId();
        this.status = Status.NEW;
    }

    public Task(String taskName, String description, int id, Status status) {
        this.taskName = taskName;
        this.description = description;
        this.id = id;
        this.status = status;
    }


    public int getId() {
        return id;
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
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
