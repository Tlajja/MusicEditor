package App;

import Base.*;
import Advanced.SerializableEditor;
import java.io.File;

/**
 * Manages music editor instances and file operations for the music composition
 * application.
 *
 * @author Eigintas Urbanavičius
 */
public class EditorManager {
    /** The current music editor instance. */
    private MusicEditor currentEditor;
    /** The last directory used for file operations. */
    private File lastDirectory = new File(System.getProperty("user.home"));

    /**
     * Enum representing available editor types.
     */
    public enum EditorType {
        /** MP3 Editor type. */
        MP3_EDITOR("MP3 Editor", MP3Editor.class),
        /** Notation Editor type. */
        NOTATION_EDITOR("Notation Editor", NotationEditor.class);

        /** The display name of the editor type. */
        private final String displayName;
        /** The class of the editor. */
        private final Class<? extends MusicEditor> editorClass;

        /**
         * Constructs an EditorType.
         *
         * @param displayName The display name of the editor type.
         * @param editorClass The class of the editor.
         */
        EditorType(String displayName, Class<? extends MusicEditor> editorClass) {
            this.displayName = displayName;
            this.editorClass = editorClass;
        }

        /**
         * Gets the display name of the editor type.
         *
         * @return The display name.
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * Creates a new editor instance of this type.
         *
         * @return A new MusicEditor instance.
         * @throws RuntimeException If editor creation fails.
         */
        public MusicEditor createEditor() {
            try {
                return editorClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create editor", e);
            }
        }
    }

    /**
     * Constructs an EditorManager with a default MP3Editor.
     */
    public EditorManager() {
        this.currentEditor = EditorType.MP3_EDITOR.createEditor();
    }

    /**
     * Gets the current editor.
     *
     * @return The current MusicEditor instance.
     */
    public MusicEditor getCurrentEditor() {
        return currentEditor;
    }

    /**
     * Switches the current editor to a new type, preserving title, author, and
     * fragments.
     *
     * @param type The new editor type.
     */
    public void switchEditorType(EditorType type) {
        String title = currentEditor.getTitle();
        String author = currentEditor.getAuthor();
        String[] fragments = currentEditor.getFragments();

        currentEditor = type.createEditor();
        currentEditor.setTitle(title);
        currentEditor.setAuthor(author);
        currentEditor.setFragments(fragments);
    }

    /**
     * Saves the current editor to a file.
     *
     * @param file The file to save to.
     * @throws RuntimeException If saving fails.
     */
    public void saveToFile(File file) {
        try {
            SerializableEditor.saveToFile(currentEditor, file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Save failed", e);
        }
    }

    /**
     * Loads an editor from a file.
     *
     * @param file The file to load from.
     * @return The loaded MusicEditor instance.
     * @throws Exception If loading fails.
     */
    public MusicEditor loadFromFile(File file) throws Exception {
        SerializableEditor.loadFromFile(file.getAbsolutePath(), loadedObject -> {
            if (loadedObject instanceof MusicEditor) {
                this.currentEditor = (MusicEditor) loadedObject;
            }
        });
        return currentEditor;
    }

    /**
     * Gets the last directory used for file operations.
     *
     * @return The last directory.
     */
    public File getLastDirectory() {
        return lastDirectory;
    }

    /**
     * Sets the last directory used for file operations.
     *
     * @param lastDirectory The new directory.
     */
    public void setLastDirectory(File lastDirectory) {
        this.lastDirectory = lastDirectory;
    }

    /**
     * Sets the current editor.
     *
     * @param editor The new MusicEditor instance.
     */
    public void setCurrentEditor(MusicEditor editor) {
        this.currentEditor = editor;
    }
}
