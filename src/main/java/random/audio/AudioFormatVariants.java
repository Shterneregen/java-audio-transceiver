package random.audio;

import javax.sound.sampled.AudioFormat;

public class AudioFormatVariants {

    private AudioFormatVariants() {
    }

    public static final AudioFormat FORMAT_1 = new AudioFormat(8000.0f, 8, 1, true, false);
    public static final AudioFormat FORMAT_2 = new AudioFormat(16000.0f, 16, 2, true, false);
}
