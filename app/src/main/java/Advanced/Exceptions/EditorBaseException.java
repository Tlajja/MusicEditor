package Advanced.Exceptions;

/**
 * Base exception class for editor-related exceptions.
 *
 * @author Eigintas Urbanavičius
 */
public class EditorBaseException extends Exception {
    /**
     * Constructs an EditorBaseException.
     *
     * @param message The error message.
     */
    public EditorBaseException(String message) {
        super(message);
    }
}
