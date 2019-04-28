package random.audio;

import javax.sound.sampled.*;

public class SpeakerWriter {

    private static AudioFormat format = AudioFormatVariants.FORMAT_1;

    private SpeakerWriter() {
    }

    public static SourceDataLine initSpeakers() throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        speakers.open(format);
        speakers.start();
        return speakers;
    }
}
