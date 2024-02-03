import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Subtask> subtasks = new ArrayList<>();
    Status status = Status.NEW;

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    private boolean isStatusNew() {
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (subtask.status != Status.NEW) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isStatusDone() {
        for (Subtask subtask : subtasks) {
            if (subtask.status != Status.DONE) {
                return false;
            }
        }
        return true;
    }

    public Status getStatus() {
        if (isStatusNew()) {
            return Status.NEW;
        }
        else if (isStatusDone()) {
            return Status.DONE;
        }
        else {
            return Status.IN_PROGRESS;
        }

    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }
    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeSubById(int id) {
        subtasks.removeIf(subtask -> subtask.id == id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", status=" + status +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}

