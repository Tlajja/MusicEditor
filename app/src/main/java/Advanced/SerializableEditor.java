package Advanced;

import java.util.function.Consumer;
import java.io.*;

/**
 * An abstract class providing serialization functionality for editors.
 *
 * @author Eigintas Urbanavičius
 */
public abstract class SerializableEditor implements Serializable {

    /**
     * Saves a serializable object to a file in a separate thread.
     *
     * @param object   The object to save.
     * @param filename The file path to save to.
     */
    public static void saveToFile(Serializable object, String filename) {
        new Thread(() -> {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Loads a serializable object from a file in a separate thread.
     *
     * @param filename The file path to load from.
     * @param callback The callback to handle the loaded object.
     */
    public static void loadFromFile(String filename, Consumer<Object> callback) {
        new Thread(() -> {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                Object loadedObject = ois.readObject();
                callback.accept(loadedObject);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
