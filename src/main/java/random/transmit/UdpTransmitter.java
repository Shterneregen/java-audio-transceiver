package random.transmit;

import random.audio.MicrophoneReader;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpTransmitter extends Thread implements Transmitter {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final int CHUNK_SIZE = 256;

    private final int port;
    private final String destinationIpAddress;

    private boolean stop = false;

    public UdpTransmitter(int port, String destinationIpAddress) {
        this.port = port;
        this.destinationIpAddress = destinationIpAddress;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket();
             TargetDataLine microphone = MicrophoneReader.getMicrophone()) {
            LOG.log(Level.INFO, "UDP transmitter started on port {0} for {1}",
                    new Object[]{String.valueOf(port), destinationIpAddress});
            InetAddress destinationAddress = InetAddress.getByName(destinationIpAddress);

            while (!stop) {
                byte[] data = new byte[CHUNK_SIZE];
                int numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                DatagramPacket packet = new DatagramPacket(data, numBytesRead, destinationAddress, port);

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
