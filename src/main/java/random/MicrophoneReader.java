package random;

import javax.sound.sampled.*;

public class MicrophoneReader extends Thread {
    private static MicrophoneReader instance = new MicrophoneReader();

    public static MicrophoneReader getInstance() {
        System.out.println("new MicrophoneReader");
        return instance;
    }

    //         AudioFormat format = new AudioFormat(16000.0f, 16, 2, true, false);
    private AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, false);
    private TargetDataLine microphone;

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
            System.out.println("Microphone reader started");
            return true;
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return false;
        }
    }

    TargetDataLine getMicrophone() {
        return microphone;
    }
}
