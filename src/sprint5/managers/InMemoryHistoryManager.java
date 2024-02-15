package sprint5.managers;

import sprint5.models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> history = new ArrayList<>(10);

    @Override
    public List<Task> getHistory() {
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
