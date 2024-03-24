package sprint.exceptions;

public class ManagerSaveException extends RuntimeException  {

    public ManagerSaveException(final String message) {
        super(message);
    }

    public String getDetailMessage() {
        return getMessage();
    }

    @Override
    public String toString() {
        return getDetailMessage();
    }
}
