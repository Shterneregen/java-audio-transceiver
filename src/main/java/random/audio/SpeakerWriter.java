package random.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SpeakerWriter {

    private SpeakerWriter() {
    }

    public static SourceDataLine initSpeakers(AudioFormat format) throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        speakers.open(format);
        speakers.start();
        return speakers;
    }
}
