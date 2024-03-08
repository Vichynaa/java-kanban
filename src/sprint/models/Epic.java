package sprint.models;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasks = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    public Epic(int epicId, String taskName, String description) {
        super(epicId, taskName, description, Status.NEW);
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

    @Override
    public String toString() {
        return "sprint4.models.Epic{" +
                "subtasks=" + subtasks +
                ", status=" + getStatus() +
                ", taskName='" + getTaskName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                '}';
    }
}

