public class IdUtils {

    private static int id = 0;
    public static int createId() {
        id += 1;
        return id;
    }
}
