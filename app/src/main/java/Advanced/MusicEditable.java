package Advanced;

import Advanced.Exceptions.EditorOperationException;

/**
 * Interface defining editable operations for music editors.
 *
 * @author Eigintas Urbanavičius
 */
public interface MusicEditable extends Editable {
    /**
     * Adds a single fragment to the composition.
     *
     * @param fragment The fragment to add.
     * @throws EditorOperationException If the fragment is invalid.
     */
    void addFragment(String fragment) throws EditorOperationException;

    /**
     * Adds multiple fragments to the composition.
     *
     * @param fragments The array of fragments to add.
     * @throws EditorOperationException If the fragment array is invalid.
     */
    void addFragment(String[] fragments) throws EditorOperationException;

    /**
     * Cuts a segment from the composition.
     *
     * @param start The start index or time.
     * @param end   The end index or time.
     * @throws EditorOperationException If the cut parameters are invalid.
     */
    void cut(int start, int end) throws EditorOperationException;

    /**
     * Plays the composition.
     *
     * @throws EditorOperationException If playback fails.
     */
    void play() throws EditorOperationException;
}
