package random.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.Arrays;

public class MicrophoneReader {

    private MicrophoneReader() {
        throw new UnsupportedOperationException();
    }

    public static TargetDataLine getMicrophone() throws LineUnavailableException {
        TargetDataLine microphone = AudioSystem.getTargetDataLine(null);
        if (!microphone.isOpen()) {
            microphone.open(microphone.getFormat());
        }
        microphone.start();

        System.out.println("Mixer info: " + Arrays.toString(AudioSystem.getMixerInfo()));
        System.out.println("Microphone line info: " + microphone.getLineInfo());
        System.out.println("Microphone format: " + microphone.getFormat());
        System.out.println("Microphone initialized");
        return microphone;

    }
}
