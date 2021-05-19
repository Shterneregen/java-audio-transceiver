package random.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SpeakerWriter {

    private SpeakerWriter() {
        throw new UnsupportedOperationException();
    }

    public static SourceDataLine getSpeaker() throws LineUnavailableException {
        SourceDataLine speaker = AudioSystem.getSourceDataLine(null);
        if (!speaker.isOpen()) {
            speaker.open(speaker.getFormat());
        }
        speaker.start();

        System.out.println("Speaker format: " + speaker.getFormat());
        System.out.println("Speaker initialized");
        return speaker;
    }
}
