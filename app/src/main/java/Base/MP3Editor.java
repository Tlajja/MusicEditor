package Base;

import Advanced.Exceptions.EditorOperationException;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that extends MusicEditor to handle MP3 audio file editing and
 * playback.
 * Provides functionality to cut audio segments and modify MP3 properties
 *
 * @author Eigintas Urbanavičius
 */
public class MP3Editor extends MusicEditor {
    /** The bitrate of the MP3 in kilobits per second. */
    private int bitrate;
    /** The duration of the MP3 in seconds. */
    private int duration;
    /** The raw audio data as a list of bytes. */
    private List<Byte> audioData;
    /** The sample rate of the MP3 in Hertz. */
    private float sampleRate;

    /** Default bitrate (128 kbps). */
    protected static final int DEFAULT_BITRATE = 128;
    /** Default duration (180 seconds). */
    protected static final int DEFAULT_DURATION = 180;
    /** Default empty audio data list. */
    protected static final List<Byte> DEFAULT_AUDIO_DATA = new ArrayList<>();
    /** Default sample rate (44100 Hz). */
    protected static final float DEFAULT_SAMPLE_RATE = 44100.0f;

    /**
     * Constructs an MP3Editor with specified parameters.
     *
     * @param title      The title of the MP3 composition.
     * @param fragments  The fragments (e.g., Intro, Verse) of the composition.
     * @param author     The author of the composition.
     * @param bitrate    The bitrate in kbps.
     * @param duration   The duration in seconds.
     * @param audioData  The raw audio data as a list of bytes.
     * @param sampleRate The sample rate in Hertz.
     */

    public MP3Editor(String title, String[] fragments, String author, int bitrate, int duration, List<Byte> audioData,
            float sampleRate) {
        super(title, fragments, author);
        this.bitrate = bitrate;
        this.duration = duration;
        this.audioData = audioData != null ? new ArrayList<>(audioData) : new ArrayList<>();
        this.sampleRate = sampleRate > 0 ? sampleRate : DEFAULT_SAMPLE_RATE;
    }

    /**
     * Constructs an MP3Editor with default bitrate, duration, and sample rate.
     *
     * @param title     The title of the MP3 composition.
     * @param fragments The fragments of the composition.
     * @param author    The author of the composition.
     */
    public MP3Editor(String title, String[] fragments, String author) {
        this(title, fragments, author, DEFAULT_BITRATE, DEFAULT_DURATION, DEFAULT_AUDIO_DATA, DEFAULT_SAMPLE_RATE);
    }

    /**
     * Constructs an MP3Editor with default values.
     */
    public MP3Editor() {
        super();
        this.bitrate = DEFAULT_BITRATE;
        this.duration = DEFAULT_DURATION;
        this.audioData = new ArrayList<>();
        this.sampleRate = DEFAULT_SAMPLE_RATE;
    }

    /**
     * Cuts a segment of the MP3 audio from start to end time.
     *
     * @param start The start time in seconds.
     * @param end   The end time in seconds.
     * @throws EditorOperationException If start is negative, end exceeds duration,
     *                                  or start is not before end.
     */
    @Override
    public void cut(int start, int end) throws EditorOperationException {
        if (start < 0) {
            throw new EditorOperationException("cut", "Start time cannot be negative");
        }
        if (end > duration) {
            throw new EditorOperationException("cut", "End time exceeds duration");
        }
        if (start >= end) {
            throw new EditorOperationException("cut", "Start time must be before end time");
        }
        int cutDuration = end - start;
        duration -= cutDuration;
        int dataLength = audioData.size();
        int dataStart = (int) ((start / (double) duration) * dataLength);
        int dataEnd = (int) ((end / (double) duration) * dataLength);
        audioData.subList(dataStart, dataEnd).clear();
        System.out.println("Cut MP3 from " + formatTime(start) + " to " + formatTime(end) + ". New duration: "
                + formatTime(duration));
    }

    /**
     * Modifies the MP3Editor's properties.
     *
     * @param title      The new title.
     * @param fragments  The new fragments.
     * @param author     The new author.
     * @param bitrate    The new bitrate in kbps.
     * @param duration   The new duration in seconds.
     * @param audioData  The new audio data.
     * @param sampleRate The new sample rate in Hertz.
     * @throws EditorOperationException If bitrate, duration, or sample rate is not
     *                                  positive, or if modification fails.
     */
    public void modify(String title, String[] fragments, String author, int bitrate, int duration, List<Byte> audioData,
            float sampleRate)
            throws EditorOperationException {
        try {
            super.modify(title, fragments, author);
            if (bitrate <= 0) {
                throw new EditorOperationException("modify", "Bitrate must be positive");
            }
            if (duration <= 0) {
                throw new EditorOperationException("modify", "Duration must be positive");
            }
            if (sampleRate <= 0) {
                throw new EditorOperationException("modify", "Sample rate must be positive");
            }
            setBitrate(bitrate);
            setDuration(duration);
            setAudioData(audioData);
            setSampleRate(sampleRate);
        } catch (EditorOperationException e) {
            throw new EditorOperationException("modify", "Failed to modify: " + e.getMessage());
        }
    }

    /**
     * Gets the bitrate of the MP3.
     *
     * @return The bitrate in kbps.
     */
    public final int getBitrate() {
        return bitrate;
    }

    /**
     * Sets the bitrate of the MP3.
     *
     * @param bitrate The bitrate in kbps.
     */
    public final void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * Gets the duration of the MP3.
     *
     * @return The duration in seconds.
     */
    public final int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the MP3.
     *
     * @param duration The duration in seconds.
     */
    public final void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets a copy of the audio data.
     *
     * @return A new list containing the audio data bytes.
     */
    public final List<Byte> getAudioData() {
        return new ArrayList<>(audioData);
    }

    /**
     * Sets the audio data.
     *
     * @param audioData The new audio data as a list of bytes.
     */
    public final void setAudioData(List<Byte> audioData) {
        this.audioData = audioData != null ? new ArrayList<>(audioData) : new ArrayList<>();
    }

    /**
     * Gets the sample rate of the MP3.
     *
     * @return The sample rate in Hertz.
     */
    public final float getSampleRate() {
        return sampleRate;
    }

    /**
     * Sets the sample rate of the MP3.
     *
     * @param sampleRate The sample rate in Hertz.
     */
    public final void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate > 0 ? sampleRate : DEFAULT_SAMPLE_RATE;
    }

    /**
     * Adds audio data to the existing audio data list.
     *
     * @param dataToAdd The byte array to add.
     */
    public final void addAudioData(byte[] dataToAdd) {
        if (dataToAdd != null) {
            for (byte b : dataToAdd) {
                audioData.add(b);
            }
        }
    }

    /**
     * Plays the MP3 audio data using the system's audio system.
     *
     * @throws EditorOperationException If no audio data is available or playback
     *                                  fails.
     */
    @Override
    public void play() throws EditorOperationException {
        if (audioData.isEmpty()) {
            throw new EditorOperationException("play", "No audio data available");
        }
        try {
            byte[] byteArray = new byte[audioData.size()];
            for (int i = 0; i < audioData.size(); i++) {
                byteArray[i] = audioData.get(i);
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
            AudioFormat format = new AudioFormat(sampleRate, 16, 2, true, false);
            AudioInputStream audioInputStream = new AudioInputStream(inputStream, format,
                    byteArray.length / format.getFrameSize());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (Exception e) {
            throw new EditorOperationException("play", "Playback failed: " + e.getMessage());
        }
    }

    /**
     * Returns a string representation of the MP3Editor.
     *
     * @return A string containing the title, author, bitrate, duration, and sample
     *         rate.
     */
    public String toString() {
        return super.toString() + " [MP3Editor with bitrate " + bitrate + " kbps, duration: " + formatTime(duration)
                + ", sample rate: " + sampleRate + " Hz]";
    }

    /**
     * Modifies a specific byte in the audio data.
     *
     * @param index   The index of the byte to modify.
     * @param newData The new byte value.
     * @throws EditorOperationException If the index is out of range.
     */
    public void mutateAudioData(int index, byte newData) throws EditorOperationException {
        if (index < 0 || index >= audioData.size()) {
            throw new EditorOperationException("mutateAudioData", "Index out of range");
        }
        audioData.set(index, newData);
    }

    /**
     * Creates a deep copy of the MP3Editor.
     *
     * @return A cloned MP3Editor instance.
     * @throws CloneNotSupportedException If cloning is not supported.
     */
    @Override
    public MP3Editor clone() throws CloneNotSupportedException {
        MP3Editor clone = (MP3Editor) super.clone();
        clone.audioData = new ArrayList<>(this.audioData);
        return clone;
    }

    /**
     * Formats the duration in MM:SS format.
     *
     * @param duration The duration in seconds.
     * @return A formatted string in MM:SS.
     */
    private String formatTime(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
