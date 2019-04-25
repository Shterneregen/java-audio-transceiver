package random.transmit;

import random.audio.MicrophoneReader;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpTransmitter extends Thread implements Transmitter {

    private static final Logger LOG = Logger.getLogger(UdpTransmitter.class.getName());

    private static final int CHUNK_SIZE = 256;
    private InetAddress address;
    private DatagramSocket socket;
    private boolean stop;

    private MicrophoneReader mr;
    private int port;

    public UdpTransmitter(int port) {
        this.port = port;
        init();
    }

    private void init() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            mr = MicrophoneReader.getInstance();
            if (!mr.init()) {
                throw new LineUnavailableException("Cannot init MicrophoneReader");
            }
        } catch (SocketException | UnknownHostException | LineUnavailableException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    @Override
    public void run() {
        stop = false;

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
        socket.close();
    }

    @Override
    public void transmit() {
        this.start();
    }

}
