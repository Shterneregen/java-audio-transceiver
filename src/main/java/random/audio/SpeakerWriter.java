package random.audio;

import javax.sound.sampled.*;

public class SpeakerWriter {

    private SpeakerWriter() {
    }

    public static SourceDataLine initSpeakers() throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, AudioFormatVariants.FORMAT_1);
        SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        speakers.open(AudioFormatVariants.FORMAT_1);
        speakers.start();
        return speakers;
    }
}
