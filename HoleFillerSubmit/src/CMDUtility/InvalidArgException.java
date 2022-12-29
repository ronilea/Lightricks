package CMDUtility;

/**
 * invalid argument kind of excption.
 */
public class InvalidArgException extends Exception {
    public InvalidArgException(String error)
    {
        super("Error: " + error);

    }

}
