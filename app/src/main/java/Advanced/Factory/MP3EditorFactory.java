package Advanced.Factory;

import Base.MP3Editor;
import Base.MusicEditor;

/**
 * Factory for creating MP3Editor instances.
 *
 * @author Eigintas Urbanavičius
 */
public class MP3EditorFactory extends MusicEditorFactory {
    /**
     * Creates an MP3Editor instance.
     *
     * @return A new MP3Editor instance.
     */
    @Override
    public MusicEditor createEditor() {
        return new MP3Editor();
    }
}
