package random.receive;

import random.audio.SpeakerWriter;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpReceiver implements Receiver {

    private static final Logger LOG = Logger.getLogger(Receiver.class.getName());

    private static final int CHUNK_SIZE = 10000;
    private static boolean stop = false;

    private String host;
    private int port;

    public TcpReceiver(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void receive() throws UnknownHostException {

        InetAddress address = InetAddress.getByName(host);
        try (Socket socket = new Socket(address, port)) {
            InputStream is = socket.getInputStream();

            SourceDataLine speakers = SpeakerWriter.initSpeakers();
            int numBytesRead;
            byte[] data = new byte[CHUNK_SIZE];

            while (!stop) {
                numBytesRead = is.read(data);
                speakers.write(data, 0, numBytesRead);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
