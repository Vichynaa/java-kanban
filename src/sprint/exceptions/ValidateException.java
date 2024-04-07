package sprint.exceptions;

public class ValidateException extends RuntimeException {
    public ValidateException(final String message) {
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
