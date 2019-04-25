package random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receiver {

    private static final Logger LOG = Logger.getLogger(Receiver.class.getName());

    private static final int PORT = 7373;
    private static final int CHUNK_SIZE = 10000;
    private static boolean stop = false;

    private Receiver() {
    }

    public static void receive(String host) throws UnknownHostException {
        // AudioFormat format = new AudioFormat(16000.0f, 16, 2, true, false);
        AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, false);
        SourceDataLine speakers;

        InetAddress address = InetAddress.getByName(host);
        try (Socket socket = new Socket(address, PORT)) {
            InputStream is = socket.getInputStream();

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();

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
