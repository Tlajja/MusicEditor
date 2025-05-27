package Advanced.Factory;

import Base.MusicEditor;

/**
 * Abstract factory for creating MusicEditor instances.
 *
 * @author Eigintas Urbanavičius
 */
public abstract class MusicEditorFactory {
    /**
     * Creates a MusicEditor instance.
     *
     * @return A new MusicEditor instance.
     */
    public abstract MusicEditor createEditor();
}
