package Advanced;

import Advanced.Exceptions.EditorOperationException;

/**
 * Interface defining basic modification operations for editors.
 *
 * @author Eigintas Urbanavičius
 */
public interface Editable {
    /**
     * Modifies the editor's properties.
     *
     * @param title     The new title.
     * @param fragments The new fragments.
     * @param author    The new author.
     * @throws EditorOperationException If modification fails.
     */
    void modify(String title, String[] fragments, String author) throws EditorOperationException;
}
