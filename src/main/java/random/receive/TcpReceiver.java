package random.receive;

import random.audio.SpeakerWriter;

import javax.sound.sampled.SourceDataLine;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpReceiver implements Receiver {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final int CHUNK_SIZE = 256;

    private final String host;
    private final int port;

    private boolean stop = false;

    public TcpReceiver(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void receive() throws UnknownHostException {
        InetAddress address = InetAddress.getByName(host);
        try (Socket socket = new Socket(address, port);
             InputStream is = socket.getInputStream();
             SourceDataLine speaker = SpeakerWriter.getSpeaker()) {

            int numBytesRead;
            byte[] data = new byte[CHUNK_SIZE];

            while (!stop) {
                numBytesRead = is.read(data);
                if (numBytesRead == -1) {
                    LOG.log(Level.SEVERE, "Transmitter was stopped");
                    break;
                }
                speaker.write(data, 0, numBytesRead);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
