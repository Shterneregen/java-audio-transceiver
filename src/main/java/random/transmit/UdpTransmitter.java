package random.transmit;

import random.audio.AudioFormatVariants;
import random.audio.MicrophoneReader;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpTransmitter extends Thread implements Transmitter {

    private static final Logger LOG = Logger.getLogger(UdpTransmitter.class.getName());
    private static final int CHUNK_SIZE = 256;

    private final int port;

    private boolean stop = false;

    public UdpTransmitter(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket();) {
            InetAddress address = InetAddress.getByName("localhost");
            MicrophoneReader mr = MicrophoneReader.getInstance();
            mr.init(AudioFormatVariants.FORMAT_8000);

            while (!stop) {
                byte[] data = new byte[CHUNK_SIZE];
                int numBytesRead = mr.getMicrophone().read(data, 0, CHUNK_SIZE);
                DatagramPacket packet = new DatagramPacket(data, numBytesRead, address, port);

//            if (received.equals("end")) {
//                stop = true;
//                continue;
//            }
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        } catch (SocketException | UnknownHostException | LineUnavailableException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void transmit() {
        this.start();
    }

}
