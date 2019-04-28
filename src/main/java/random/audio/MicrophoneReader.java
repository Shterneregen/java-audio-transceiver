package random.audio;

import javax.sound.sampled.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MicrophoneReader {

    private static final Logger LOG = Logger.getLogger(MicrophoneReader.class.getName());
    private static AudioFormat format = AudioFormatVariants.FORMAT_1;
    private TargetDataLine microphone;

    private static MicrophoneReader instance = new MicrophoneReader();

    public static MicrophoneReader getInstance() {
        LOG.info("new MicrophoneReader");
        return instance;
    }

    private MicrophoneReader() {
    }

    public boolean init() {
        try {
            if (microphone != null && microphone.isOpen()) return true;
            microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
            LOG.info("Microphone reader started");
            return true;
        } catch (LineUnavailableException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public TargetDataLine getMicrophone() {
        return microphone;
    }
}
