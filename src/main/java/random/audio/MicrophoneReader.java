package random.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MicrophoneReader {

    private static final Logger LOG = Logger.getLogger(MicrophoneReader.class.getName());
    private static final MicrophoneReader instance = new MicrophoneReader();

    private TargetDataLine microphone;

    public static MicrophoneReader getInstance() {
        return instance;
    }

    private MicrophoneReader() {
    }

    public void init(AudioFormat format) throws LineUnavailableException {
        try {
            if (microphone != null && microphone.isOpen()) {
                return;
            }

            microphone = AudioSystem.getTargetDataLine(format);
            microphone.open(format);
            microphone.start();

//            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            LOG.info(String.valueOf(microphone.getLineInfo()));
            LOG.info("Microphone reader started");
        } catch (LineUnavailableException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            throw new LineUnavailableException("Cannot init MicrophoneReader");
        }
    }

    public TargetDataLine getMicrophone() {
        return microphone;
    }
}
