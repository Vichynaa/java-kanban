package sprint5.managers;

import sprint5.models.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private final ArrayList<Task> history = new ArrayList<>(10);

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void add(Task object) {
        if (10 == history.size()) {
            history.removeFirst();
            history.add(object);
        }
        else if (object != null){
            history.add(object);
        }
    }


}
