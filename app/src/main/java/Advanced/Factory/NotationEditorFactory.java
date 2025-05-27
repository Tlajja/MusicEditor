package Advanced.Factory;

import Base.NotationEditor;
import Base.MusicEditor;

/**
 * Factory for creating NotationEditor instances.
 *
 * @author Eigintas Urbanavičius
 */
public class NotationEditorFactory extends MusicEditorFactory {
    /**
     * Creates a NotationEditor instance.
     *
     * @return A new NotationEditor instance.
     */
    @Override
    public MusicEditor createEditor() {
        return new NotationEditor();
    }
}
