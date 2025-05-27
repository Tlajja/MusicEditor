package Base;

import Advanced.*;
import Advanced.Exceptions.EditorOperationException;

/**
 * An abstract base class for music editors, providing common functionality for
 * managing music compositions.
 * 
 * <p>
 * This Class implements {@link MusicEditable}, {@link Cloneable} interfaces,
 * and extends {@link SerializableEditor} providing a foundation for more
 * specialized Editors
 * </p>
 *
 * @author Eigintas Urbanavičius
 */
public abstract class MusicEditor extends SerializableEditor implements MusicEditable, Cloneable {
    /** The title of the music composition. */
    protected String title;
    /** The author of the music composition. */
    protected String author;
    /** The fragments of the composition (e.g., Intro, Verse, Outro). */
    protected String[] fragments;

    /** Tracks the number of MusicEditor instances created. */
    protected static int instanceCount = 0;

    /** Default title for compositions. */
    protected static final String DEFAULT_TITLE = "Untitled";
    /** Default author for compositions. */
    protected static final String DEFAULT_AUTHOR = "Unknown author";

    /**
     * Constructs a MusicEditor with default values.
     */
    protected MusicEditor() {
        this(DEFAULT_TITLE, new String[0], DEFAULT_AUTHOR);
    }

    /**
     * Constructs a MusicEditor with specified parameters.
     *
     * @param title     The title of the composition.
     * @param fragments The fragments of the composition.
     * @param author    The author of the composition.
     */
    protected MusicEditor(String title, String[] fragments, String author) {
        this.title = title != null ? title : DEFAULT_TITLE;
        this.fragments = fragments != null ? fragments : new String[0];
        this.author = author != null ? author : DEFAULT_AUTHOR;
        instanceCount++;
    }

    /**
     * Modifies the music editor's properties.
     *
     * @param title     The new title.
     * @param fragments The new fragments.
     * @param author    The new author.
     */
    public void modify(String title, String[] fragments, String author) {
        setTitle(title);
        setFragments(fragments);
        setAuthor(author);
    }

    /**
     * Adds a single fragment to the composition.
     *
     * @param fragment The fragment to add.
     * @throws EditorOperationException If the fragment is null.
     */
    public void addFragment(String fragment) throws EditorOperationException {
        if (fragment == null) {
            throw new EditorOperationException("addFragment", "Fragment cannot be null");
        }

        String[] newFragments = new String[fragments.length + 1];
        System.arraycopy(fragments, 0, newFragments, 0, fragments.length);
        newFragments[fragments.length] = fragment;
        this.fragments = newFragments;
    }

    /**
     * Adds multiple fragments to the composition.
     *
     * @param fragmentsToAdd The array of fragments to add.
     * @throws EditorOperationException If the fragment array is null.
     */
    public void addFragment(String[] fragmentsToAdd) throws EditorOperationException {
        if (fragmentsToAdd == null) {
            throw new EditorOperationException("addFragment", "Fragment array cannot be null");
        }

        for (String fragment : fragmentsToAdd) {
            addFragment(fragment);
        }
    }

    /**
     * Gets the title of the composition.
     *
     * @return The title.
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Sets the title of the composition.
     *
     * @param title The new title.
     */
    public final void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the composition.
     *
     * @return The author.
     */
    public final String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the composition.
     *
     * @param author The new author.
     */
    public final void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the fragments of the composition.
     *
     * @return A copy of the fragments array.
     */
    public String[] getFragments() {
        return fragments;
    }

    /**
     * Sets the fragments of the composition.
     *
     * @param fragments The new fragments array.
     */
    public void setFragments(String[] fragments) {
        this.fragments = fragments;
    }

    /**
     * Gets the number of MusicEditor instances created.
     *
     * @return The instance count.
     */
    public static int getInstanceCount() {
        return instanceCount;
    }

    /**
     * Cuts a segment of fragments from the composition.
     *
     * @param start The start index of the fragment to cut.
     * @param end   The end index of the fragment to cut.
     * @throws EditorOperationException If start is negative, end exceeds fragment
     *                                  count, or start is not before end.
     */
    public void cut(int start, int end) throws EditorOperationException {
        if (start < 0) {
            throw new EditorOperationException("cut", "Start cannot be negative");
        }
        if (end > fragments.length) {
            throw new EditorOperationException("cut", "End exceeds fragment count");
        }
        if (start >= end) {
            throw new EditorOperationException("cut", "Start must be before end");
        }

        String[] newFragments = new String[fragments.length - (end - start)];
        System.arraycopy(fragments, 0, newFragments, 0, start);
        System.arraycopy(fragments, end, newFragments, start, fragments.length - end);
        this.fragments = newFragments;
    }

    /**
     * Returns a string representation of the MusicEditor.
     *
     * @return A string containing the title and author.
     */
    public String toString() {
        return "MusicEditor[Title: " + title + ", Author: " + author + "]";
    }

    /**
     * Creates a deep copy of the MusicEditor.
     *
     * @return A cloned MusicEditor instance.
     * @throws CloneNotSupportedException If cloning is not supported.
     */
    protected Object clone() throws CloneNotSupportedException {
        MusicEditor cloned = (MusicEditor) super.clone();
        cloned.fragments = fragments != null ? fragments.clone() : null;
        return cloned;
    }
}
