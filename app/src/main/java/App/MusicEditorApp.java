package App;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.application.Platform;
import java.io.File;
import Base.*;
import Advanced.*;
import Advanced.Exceptions.EditorOperationException;

/**
 * The main JavaFX application class for the Music Composition Editor.
 *
 * @author Eigintas Urbanavičius
 */
public class MusicEditorApp extends Application {
    /** Manages the music editor instances. */
    private EditorManager editorManager;
    /** The UI component for the editor. */
    private EditorUI editorUI;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        editorManager = new EditorManager();
        editorUI = new EditorUI(editorManager);

        editorUI.getEditorTypeCombo().setOnAction(e -> {
            editorManager.switchEditorType(editorUI.getEditorTypeCombo().getValue());
            editorUI.updateUIFromEditor();
        });

        VBox root = new VBox(10,
                editorUI.createToolBar(this::handleSave, this::handleLoad, this::handlePlay),
                editorUI.createEditorForm(),
                editorUI.getStatusLabel());
        root.setPadding(new Insets(10));

        editorUI.updateUIFromEditor();

        Scene scene = new Scene(root, 700, 600);
        scene.getStylesheets().add("resources/style.css");
        primaryStage.setTitle("Music Composition Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Handles saving the current composition to a file.
     */
    private void handleSave() {
        FileChooser fileChooser = createFileChooser("Save Music Composition");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                editorUI.updateEditorFromUI();
                editorManager.saveToFile(ensureMedExtension(file));
                editorUI.setStatus("Saved composition: " + file.getName(), false);
            } catch (Exception e) {
                editorUI.setStatus("Save failed: " + e.getMessage(), true);
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles loading a composition from a file.
     */
    private void handleLoad() {
        FileChooser fileChooser = createFileChooser("Open Music Composition");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                editorUI.setStatus("Loading...", false);
                SerializableEditor.loadFromFile(file.getAbsolutePath(), loadedObject -> {
                    Platform.runLater(() -> {
                        try {
                            if (loadedObject instanceof MusicEditor) {
                                editorManager.setCurrentEditor((MusicEditor) loadedObject);
                                if (loadedObject instanceof NotationEditor) {
                                    editorUI.getEditorTypeCombo().setValue(EditorManager.EditorType.NOTATION_EDITOR);
                                } else {
                                    editorUI.getEditorTypeCombo().setValue(EditorManager.EditorType.MP3_EDITOR);
                                }
                                editorUI.updateUIFromEditor();
                                editorUI.setStatus("Loaded composition: " + file.getName(), false);
                            } else {
                                editorUI.setStatus("Error: Invalid file format", true);
                            }
                        } catch (Exception e) {
                            editorUI.setStatus("Load error: " + e.getMessage(), true);
                            e.printStackTrace();
                        }
                    });
                });
            } catch (Exception e) {
                editorUI.setStatus("Load failed: " + e.getMessage(), true);
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles playing the current composition.
     */
    private void handlePlay() {
        try {
            editorUI.updateEditorFromUI();
            editorManager.getCurrentEditor().play();
            editorUI.setStatus("Playing composition...", false);
        } catch (EditorOperationException e) {
            editorUI.setStatus("Playback failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    /**
     * Creates a file chooser with specified title and .med extension filter.
     *
     * @param title The title of the file chooser.
     * @return The configured FileChooser.
     */
    private FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Music Files", "*.med"));
        fileChooser.setInitialDirectory(editorManager.getLastDirectory());
        return fileChooser;
    }

    /**
     * Ensures the file has a .med extension.
     *
     * @param file The file to check.
     * @return The file with .med extension.
     */
    private File ensureMedExtension(File file) {
        if (!file.getName().toLowerCase().endsWith(".med")) {
            return new File(file.getAbsolutePath() + ".med");
        }
        return file;
    }

    /**
     * Main entry point for the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
