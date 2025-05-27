package Advanced.Exceptions;

/**
 * Exception thrown when an editor operation fails.
 *
 * @author Eigintas Urbanavičius
 */
public class EditorOperationException extends EditorBaseException {
    /** The operation that caused the exception. */
    private final String operation;

    /**
     * Constructs an EditorOperationException.
     *
     * @param operation The operation that failed.
     * @param message   The error message.
     */
    public EditorOperationException(String operation, String message) {
        super("Operation '" + operation + "' error: " + message);
        this.operation = operation;
    }

    /**
     * Gets the operation that caused the exception.
     *
     * @return The operation name.
     */
    public String getOperation() {
        return operation;
    }
}
