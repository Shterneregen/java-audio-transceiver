package random.audio;

import javax.sound.sampled.AudioFormat;

public class AudioFormats {

    private AudioFormats() {
    }

    public static final AudioFormat FORMAT_8000 = new AudioFormat(8000.0f, 8, 1, true, false);
    public static final AudioFormat FORMAT_16000 = new AudioFormat(16000.0f, 16, 2, true, false);
}
