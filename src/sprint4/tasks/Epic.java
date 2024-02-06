package sprint4.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    public Epic(String taskName, String description, int id) {
        super(taskName, description, id, Status.NEW);
    }



    public void addSubtask(int subtaskId) {
        subtasks.add(subtaskId);
    }

    public void removeSubById(Integer id) {
        subtasks.remove(id);
    }


    public void calcStatus(ArrayList<Subtask> subtasksMap) {
        boolean statusNew = false;
        boolean statusDone = false;

        for (Subtask subtask : subtasksMap) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                setStatus(Status.IN_PROGRESS);
            }
            else if (subtask.getStatus() == Status.NEW) {
                statusNew = true;
            }
            else if (subtask.getStatus() == Status.DONE) {
                statusDone = true;
            }
        }
        if (statusNew && statusDone) {
            setStatus(Status.IN_PROGRESS);
        }
        else if (statusDone) {
            setStatus(Status.DONE);
        }
        else {
            setStatus(Status.NEW);
        }
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return "sprint4.tasks.Epic{" +
                "subtasks=" + subtasks +
                ", status=" + getStatus() +
                ", taskName='" + getTaskName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                '}';
    }
}

