package sprint.exceptions;

<<<<<<< HEAD
=======
>>>>>>> 06f723c81fbff3298dca296194359be31bffff2c
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
