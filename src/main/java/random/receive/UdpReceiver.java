package random.receive;

import random.audio.SpeakerWriter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpReceiver implements Receiver {

    private static final Logger LOG = Logger.getLogger(UdpReceiver.class.getName());
    private static final int CHUNK_SIZE = 256;

    private final int port;

    private boolean stop = false;

    public UdpReceiver(int port) {
        this.port = port;
    }

    @Override
    public void receive(AudioFormat format) {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            SourceDataLine speakers = SpeakerWriter.initSpeakers(format);
            byte[] data = new byte[CHUNK_SIZE];

            while (!stop) {
                socket.receive(new DatagramPacket(data, data.length));
                speakers.write(data, 0, data.length);
            }
        } catch (LineUnavailableException | IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
