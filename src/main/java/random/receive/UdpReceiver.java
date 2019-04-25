package random.receive;

import random.audio.SpeakerWriter;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpReceiver implements Receiver {

    private static final Logger LOG = Logger.getLogger(UdpReceiver.class.getName());

    private static final int CHUNK_SIZE = 256;
    private static boolean stop = false;

    private DatagramSocket socket;
    private int port;

    public UdpReceiver(int port) {
        this.port = port;
        init();
    }

    private void init() {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void receive() {
        try {
            SourceDataLine speakers = SpeakerWriter.initSpeakers();
            byte[] data = new byte[CHUNK_SIZE];

            while (!stop) {
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                speakers.write(data, 0, data.length);
            }
        } catch (LineUnavailableException | IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
