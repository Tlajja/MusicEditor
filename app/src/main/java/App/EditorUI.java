package App;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import Base.*;
import java.util.*;

/**
 * The user interface component for the music composition editor.
 *
 * @author Eigintas Urbanavičius
 */
public class EditorUI {
    /** The editor manager instance. */
    private final EditorManager editorManager;
    /** Text field for the composition title. */
    private final TextField titleField;
    /** Text field for the composition author. */
    private final TextField authorField;
    /** List view for composition fragments. */
    private final ListView<String> fragmentsListView;
    /** List view for editor-specific data (notes or audio data). */
    private final ListView<String> editorSpecificListView;
    /** Label for status messages. */
    private final Label statusLabel;
    /** Label for editor-specific field. */
    private final Label specificFieldLabel;
    /** Combo box for selecting editor type. */
    private final ComboBox<EditorManager.EditorType> editorTypeCombo;
    /** Combo box for selecting musical key (NotationEditor). */
    private final ComboBox<String> musicalKeyCombo;
    /** Spinner for selecting tempo (NotationEditor). */
    private final Spinner<Integer> tempoSpinner;
    /** Combo box for selecting instrument (NotationEditor). */
    private final ComboBox<String> instrumentCombo;
    /** Container for notation-specific controls. */
    private final VBox notationControls;

    /**
     * Constructs the EditorUI with an EditorManager.
     *
     * @param editorManager The editor manager instance.
     */
    public EditorUI(EditorManager editorManager) {
        this.editorManager = editorManager;
        titleField = new TextField();
        authorField = new TextField();
        fragmentsListView = new ListView<>();
        editorSpecificListView = new ListView<>();
        statusLabel = new Label("Ready");
        specificFieldLabel = new Label();
        editorTypeCombo = new ComboBox<>();
        musicalKeyCombo = new ComboBox<>();
        tempoSpinner = new Spinner<>(20, 300, 120);
        instrumentCombo = new ComboBox<>();
        notationControls = new VBox(5);
        setupUI();
    }

    /**
     * Sets up the UI components and their event handlers.
     */
    private void setupUI() {
        fragmentsListView.setPrefHeight(150);
        editorSpecificListView.setPrefHeight(150);
        fragmentsListView.setEditable(true);
        editorSpecificListView.setEditable(true);

        fragmentsListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<String>() {
            @Override
            public String toString(String item) {
                return item == null ? "" : item;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));

        fragmentsListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                int index = fragmentsListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    System.out.println("Double-click on fragment index: " + index);
                    fragmentsListView.edit(index);
                }
            }
        });

        editorSpecificListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<String>() {
            @Override
            public String toString(String item) {
                return item == null ? "" : item;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));

        editorSpecificListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                int index = editorSpecificListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    System.out.println("Double-click on editor-specific index: " + index);
                    editorSpecificListView.edit(index);
                }
            }
        });

        fragmentsListView.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            int index = event.getIndex();
            if (newValue != null && !newValue.trim().isEmpty()) {
                fragmentsListView.getItems().set(index, newValue);
                System.out.println("Committed fragment edit at index " + index + ": " + newValue);
            } else {
                setStatus("Fragment cannot be empty", true);
            }
        });

        editorSpecificListView.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            int index = event.getIndex();
            if (validateInput(newValue)) {
                editorSpecificListView.getItems().set(index, newValue);
                System.out.println("Committed editor-specific edit at index " + index + ": " + newValue);
            } else {
                setStatus("Invalid input: "
                        + (editorManager.getCurrentEditor() instanceof NotationEditor ? "Use format like C4 or C4:80"
                                : "Use hex value (00-ff)"),
                        true);
            }
        });

        ContextMenu fragmentContextMenu = new ContextMenu();
        MenuItem editFragmentItem = new MenuItem("Edit Fragment");
        editFragmentItem.setOnAction(e -> {
            int selectedIndex = fragmentsListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                fragmentsListView.edit(selectedIndex);
                System.out.println("Context menu edit triggered for fragment index: " + selectedIndex);
            }
        });
        fragmentContextMenu.getItems().add(editFragmentItem);
        fragmentsListView.setContextMenu(fragmentContextMenu);

        ContextMenu specificContextMenu = new ContextMenu();
        MenuItem editSpecificItem = new MenuItem("Edit Item");
        editSpecificItem.setOnAction(e -> {
            int selectedIndex = editorSpecificListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                editorSpecificListView.edit(selectedIndex);
                System.out.println("Context menu edit triggered for editor-specific index: " + selectedIndex);
            }
        });
        specificContextMenu.getItems().add(editSpecificItem);
        editorSpecificListView.setContextMenu(specificContextMenu);

        editorTypeCombo.getItems().addAll(EditorManager.EditorType.values());
        editorTypeCombo.setConverter(new StringConverter<EditorManager.EditorType>() {
            @Override
            public String toString(EditorManager.EditorType type) {
                return type == null ? "" : type.getDisplayName();
            }

            @Override
            public EditorManager.EditorType fromString(String string) {
                return editorTypeCombo.getItems().stream()
                        .filter(type -> type.getDisplayName().equals(string))
                        .findFirst()
                        .orElse(EditorManager.EditorType.MP3_EDITOR);
            }
        });
        editorTypeCombo.setValue(EditorManager.EditorType.MP3_EDITOR);
        editorTypeCombo.setOnAction(e -> {
            editorManager.switchEditorType(editorTypeCombo.getValue());
            updateUIFromEditor();
        });

        musicalKeyCombo.getItems().addAll(
                "C Major", "G Major", "D Major", "A Major", "E Major",
                "B Major", "F Major", "C Minor", "G Minor", "D Minor", "A Minor", "E Minor", "B Minor", "F Minor");
        musicalKeyCombo.setValue("C Major");

        tempoSpinner.setEditable(true);
        tempoSpinner.getValueFactory().setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return value == null ? "120" : value.toString();
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    return 120;
                }
            }
        });

        ObservableList<String> instruments = FXCollections.observableArrayList(
                "Piano (0)", "Violin (40)", "Flute (73)", "Trumpet (56)", "Acoustic Guitar (24)");
        instrumentCombo.setItems(instruments);
        instrumentCombo.setValue("Piano (0)");

        notationControls.getChildren().addAll(
                new Label("Musical Key:"), musicalKeyCombo,
                new Label("Tempo (BPM):"), tempoSpinner,
                new Label("Instrument:"), instrumentCombo);
        notationControls.setVisible(false);

        titleField.setPromptText("Enter composition title");
        authorField.setPromptText("Enter author name");
        musicalKeyCombo.setPromptText("Select musical key");
        instrumentCombo.setPromptText("Select instrument");
        statusLabel.setId("status-label");
        fragmentsListView.setId("list-view");
        editorSpecificListView.setId("list-view");
        editorTypeCombo.setId("editor-combo");
        musicalKeyCombo.setId("editor-combo");
        tempoSpinner.setId("editor-combo");
        instrumentCombo.setId("editor-combo");
    }

    /**
     * Updates the UI based on the current editor's state.
     */
    public void updateUIFromEditor() {
        MusicEditor editor = editorManager.getCurrentEditor();
        titleField.setText(editor.getTitle());
        authorField.setText(editor.getAuthor());

        ObservableList<String> fragments = FXCollections.observableArrayList(editor.getFragments());
        fragmentsListView.setItems(fragments);

        editorSpecificListView.getItems().clear();
        notationControls.setVisible(editor instanceof NotationEditor);
        if (editor instanceof NotationEditor) {
            NotationEditor ne = (NotationEditor) editor;
            specificFieldLabel.setText("Musical Notes:");
            editorSpecificListView.getItems().addAll(ne.getNotes());
            musicalKeyCombo.setValue(ne.getMusicalKey());
            tempoSpinner.getValueFactory().setValue(ne.getTempo());
            String instrumentDisplay = switch (ne.getInstrument()) {
                case 0 -> "Piano (0)";
                case 40 -> "Violin (40)";
                case 73 -> "Flute (73)";
                case 56 -> "Trumpet (56)";
                case 24 -> "Acoustic Guitar (24)";
                default -> "Piano (0)";
            };
            instrumentCombo.setValue(instrumentDisplay);
        } else if (editor instanceof MP3Editor) {
            MP3Editor me = (MP3Editor) editor;
            specificFieldLabel.setText("Audio Data (hex):");
            ObservableList<String> hexData = FXCollections.observableArrayList();
            for (Byte b : me.getAudioData()) {
                hexData.add(String.format("%02x", b));
            }
            editorSpecificListView.setItems(hexData);
        }
    }

    /**
     * Updates the current editor based on the UI state.
     */
    public void updateEditorFromUI() {
        MusicEditor editor = editorManager.getCurrentEditor();
        editor.setTitle(titleField.getText());
        editor.setAuthor(authorField.getText());
        editor.setFragments(fragmentsListView.getItems().toArray(new String[0]));

        if (editor instanceof NotationEditor) {
            NotationEditor ne = (NotationEditor) editor;
            ne.setNotes(new ArrayList<>(editorSpecificListView.getItems()));
            ne.setMusicalKey(musicalKeyCombo.getValue());
            ne.setTempo(tempoSpinner.getValue());
            int instrumentNumber = switch (instrumentCombo.getValue()) {
                case "Violin (40)" -> 40;
                case "Flute (73)" -> 73;
                case "Trumpet (56)" -> 56;
                case "Acoustic Guitar (24)" -> 24;
                default -> 0;
            };
            ne.setInstrument(instrumentNumber);
        } else if (editor instanceof MP3Editor) {
            MP3Editor me = (MP3Editor) editor;
            List<Byte> audioData = new ArrayList<>();
            for (String hex : editorSpecificListView.getItems()) {
                if (!hex.trim().isEmpty()) {
                    try {
                        audioData.add((byte) Integer.parseInt(hex, 16));
                    } catch (NumberFormatException e) {
                        setStatus("Invalid hex value: " + hex, true);
                    }
                }
            }
            me.setAudioData(audioData);
        }
    }

    /**
     * Sets the status message and style.
     *
     * @param message The status message.
     * @param isError Whether the message indicates an error.
     */
    public void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    /**
     * Creates the editor form layout.
     *
     * @return The BorderPane containing the editor form.
     */
    public BorderPane createEditorForm() {
        BorderPane form = new BorderPane();
        form.setPadding(new Insets(10));

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.addRow(0, new Label("Title:"), titleField);
        inputGrid.addRow(1, new Label("Author:"), authorField);
        inputGrid.setAlignment(Pos.CENTER_LEFT);

        VBox fragmentsSection = new VBox(5);
        fragmentsSection.getChildren().addAll(
                new Label("Fragments:"),
                fragmentsListView,
                createFragmentButtons());

        VBox specificSection = new VBox(5);
        specificSection.getChildren().addAll(
                specificFieldLabel,
                editorSpecificListView,
                createSpecificDataButtons());

        VBox center = new VBox(15, inputGrid, fragmentsSection, notationControls, specificSection);
        form.setCenter(center);
        form.setBottom(statusLabel);
        BorderPane.setAlignment(statusLabel, Pos.CENTER);

        return form;
    }

    /**
     * Creates buttons for managing fragments.
     *
     * @return An HBox containing fragment management buttons.
     */
    private HBox createFragmentButtons() {
        Button addFragment = new Button("Add Fragment");
        addFragment.setOnAction(e -> fragmentsListView.getItems().add("New Fragment"));
        Button removeFragment = new Button("Remove Selected");
        removeFragment.setOnAction(e -> {
            int selected = fragmentsListView.getSelectionModel().getSelectedIndex();
            if (selected >= 0) {
                fragmentsListView.getItems().remove(selected);
            }
        });
        return new HBox(10, addFragment, removeFragment);
    }

    /**
     * Creates buttons for managing editor-specific data.
     *
     * @return An HBox containing editor-specific data buttons.
     */
    private HBox createSpecificDataButtons() {
        Button addData = new Button("Add Data");
        addData.setOnAction(e -> {
            if (editorManager.getCurrentEditor() instanceof NotationEditor) {
                editorSpecificListView.getItems().add("C4");
            } else {
                editorSpecificListView.getItems().add("00");
            }
        });
        Button removeData = new Button("Remove Selected");
        removeData.setOnAction(e -> {
            int selected = editorSpecificListView.getSelectionModel().getSelectedIndex();
            if (selected >= 0) {
                editorSpecificListView.getItems().remove(selected);
            }
        });
        Button clearData = new Button("Clear");
        clearData.setOnAction(e -> editorSpecificListView.getItems().clear());
        return new HBox(10, addData, removeData, clearData);
    }

    /**
     * Creates the toolbar with editor controls.
     *
     * @param onSave Runnable for save action.
     * @param onLoad Runnable for load action.
     * @param onPlay Runnable for play action.
     * @return The configured ToolBar.
     */
    public ToolBar createToolBar(Runnable onSave, Runnable onLoad, Runnable onPlay) {
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> onSave.run());

        Button loadButton = new Button("Load");
        loadButton.setOnAction(e -> onLoad.run());

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> onPlay.run());

        return new ToolBar(
                new Label("Editor Type:"),
                editorTypeCombo,
                new Separator(),
                saveButton,
                loadButton,
                playButton);
    }

    /**
     * Gets the status label.
     *
     * @return The status Label.
     */
    public Label getStatusLabel() {
        return statusLabel;
    }

    /**
     * Gets the editor type combo box.
     *
     * @return The editor type ComboBox.
     */
    public ComboBox<EditorManager.EditorType> getEditorTypeCombo() {
        return editorTypeCombo;
    }

    /**
     * Validates input for editor-specific data.
     *
     * @param value The input value to validate.
     * @return True if valid, false otherwise.
     */
    private boolean validateInput(String value) {
        if (value == null || value.trim().isEmpty())
            return false;
        if (editorManager.getCurrentEditor() instanceof NotationEditor) {
            return value.matches("([A-G](#)?[0-8](\\+[A-G](#)?[0-8])*)(\\:\\d{1,3})?(\\:\\d{1,4})?");
        } else {
            return value.matches("[0-9a-fA-F]{2}");
        }
    }
}
