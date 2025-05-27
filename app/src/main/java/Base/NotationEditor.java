package Base;

import javax.sound.midi.*;
import java.util.*;

/**
 * A class that extends MusicEditor to handle musical notation editing and
 * playback using MIDI.
 *
 * @author Eigintas Urbanavičius
 */
public class NotationEditor extends MusicEditor {
    /** The list of musical notes (e.g., C4, F#5). */
    private List<String> notes = new ArrayList<>();
    /** The musical key of the composition (e.g., C Major). */
    private String musicalKey = "C Major";
    /** The tempo of the composition in beats per minute (BPM). */
    private int tempo = 120;
    /** The MIDI instrument number (e.g., 0 for Piano). */
    private int instrument = 0;

    /**
     * Plays the musical notes using the MIDI synthesizer.
     *
     * @throws RuntimeException If MIDI is unavailable or playback is interrupted.
     */
    @Override
    public void play() {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel[] channels = synth.getChannels();
            channels[0].programChange(instrument);
            System.out.println("Set instrument to: " + instrument);

            double durationScale = 120.0 / tempo;

            int pitchShift = calculatePitchShift(musicalKey);

            for (String note : notes) {
                int[] midiNotes = parseNoteToMidi(note);
                int velocity = parseVelocity(note);
                int duration = (int) (parseDuration(note) * durationScale);

                int[] shiftedMidiNotes = new int[midiNotes.length];
                for (int i = 0; i < midiNotes.length; i++) {
                    shiftedMidiNotes[i] = midiNotes[i] + pitchShift;
                    shiftedMidiNotes[i] = Math.max(0, Math.min(127, shiftedMidiNotes[i]));
                }

                for (int midiNote : shiftedMidiNotes) {
                    channels[0].noteOn(midiNote, velocity);
                }
                Thread.sleep(duration);
                for (int midiNote : shiftedMidiNotes) {
                    channels[0].noteOff(midiNote);
                }
            }

            synth.close();
        } catch (MidiUnavailableException | InterruptedException e) {
            throw new RuntimeException("Playback failed: " + e.getMessage());
        }
    }

    /**
     * Calculates the pitch shift based on the musical key.
     *
     * @param musicalKey The musical key (e.g., C Major, A Minor).
     * @return The MIDI pitch offset.
     */
    private int calculatePitchShift(String musicalKey) {
        if (musicalKey == null || musicalKey.isEmpty()) {
            return 0;
        }

        String[] parts = musicalKey.split(" ");
        if (parts.length != 2) {
            return 0;
        }
        String rootNote = parts[0];
        String mode = parts[1];

        int rootOffset;
        switch (rootNote) {
            case "C":
                rootOffset = 0;
                break;
            case "D":
                rootOffset = 2;
                break;
            case "E":
                rootOffset = 4;
                break;
            case "F":
                rootOffset = 5;
                break;
            case "G":
                rootOffset = 7;
                break;
            case "A":
                rootOffset = 9;
                break;
            case "B":
                rootOffset = 11;
                break;
            default:
                rootOffset = 0;
        }

        if (mode.equalsIgnoreCase("Minor")) {
            rootOffset = (rootOffset - 3 + 12) % 12;
        }

        return rootOffset;
    }

    /**
     * Parses a note string to MIDI note numbers.
     *
     * @param note The note string (e.g., C4, F#4+C#5).
     * @return An array of MIDI note numbers.
     * @throws IllegalArgumentException If the note format is invalid.
     */
    private int[] parseNoteToMidi(String note) {
        if (note.contains("+")) {
            String[] noteParts = note.split("\\+");
            int[] midiNotes = new int[noteParts.length];
            for (int i = 0; i < noteParts.length; i++) {
                String singleNote = noteParts[i].split(":")[0];
                midiNotes[i] = parseSingleNoteToMidi(singleNote);
            }
            return midiNotes;
        }
        return new int[] { parseSingleNoteToMidi(note.split(":")[0]) };
    }

    /**
     * Parses a single note to a MIDI note number.
     *
     * @param note The note string (e.g., C4, F#5).
     * @return The MIDI note number.
     * @throws IllegalArgumentException If the note or octave is invalid.
     */
    private int parseSingleNoteToMidi(String note) {
        if (note == null || note.isEmpty()) {
            throw new IllegalArgumentException("Invalid note: " + note);
        }
        char pitch = note.charAt(0);
        boolean isSharp = note.length() > 1 && note.charAt(1) == '#';
        String octaveStr = isSharp ? note.substring(2) : note.substring(1);
        int octave;
        try {
            octave = Integer.parseInt(octaveStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid octave in note: " + note);
        }
        int base;
        switch (pitch) {
            case 'C':
                base = 0;
                break;
            case 'D':
                base = 2;
                break;
            case 'E':
                base = 4;
                break;
            case 'F':
                base = 5;
                break;
            case 'G':
                base = 7;
                break;
            case 'A':
                base = 9;
                break;
            case 'B':
                base = 11;
                break;
            default:
                throw new IllegalArgumentException("Invalid note: " + note);
        }
        if (isSharp) {
            base += 1;
        }
        return 12 * (octave + 1) + base;
    }

    /**
     * Parses the velocity from a note string.
     *
     * @param note The note string (e.g., C4:80).
     * @return The velocity (0-127), defaulting to 100 if invalid.
     */
    private int parseVelocity(String note) {
        String[] parts = note.split(":");
        if (parts.length > 1) {
            try {
                int velocity = Integer.parseInt(parts[1]);
                return Math.max(0, Math.min(127, velocity));
            } catch (NumberFormatException e) {
                return 100;
            }
        }
        return 100;
    }

    /**
     * Parses the duration from a note string.
     *
     * @param note The note string (e.g., C4:80:500).
     * @return The duration in milliseconds, defaulting to 500 if invalid.
     */
    private int parseDuration(String note) {
        String[] parts = note.split(":");
        if (parts.length > 2) {
            try {
                int duration = Integer.parseInt(parts[2]);
                return Math.max(1, Math.min(9999, duration));
            } catch (NumberFormatException e) {
                return 500;
            }
        }
        return 500;
    }

    /**
     * Gets the list of musical notes.
     *
     * @return The list of notes.
     */
    public List<String> getNotes() {
        return notes;
    }

    /**
     * Sets the list of musical notes.
     *
     * @param notes The new list of notes.
     */
    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    /**
     * Gets the musical key.
     *
     * @return The musical key.
     */
    public String getMusicalKey() {
        return musicalKey;
    }

    /**
     * Sets the musical key.
     *
     * @param musicalKey The new musical key.
     */
    public void setMusicalKey(String musicalKey) {
        this.musicalKey = musicalKey;
    }

    /**
     * Gets the tempo in BPM.
     *
     * @return The tempo.
     */
    public int getTempo() {
        return tempo;
    }

    /**
     * Sets the tempo in BPM.
     *
     * @param tempo The new tempo.
     */
    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    /**
     * Gets the MIDI instrument number.
     *
     * @return The instrument number.
     */
    public int getInstrument() {
        return instrument;
    }

    /**
     * Sets the MIDI instrument number.
     *
     * @param instrument The new instrument number.
     */
    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }
}
