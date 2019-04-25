package random;

import javax.sound.sampled.LineUnavailableException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender extends Thread {

    private static final Logger LOG = Logger.getLogger(Sender.class.getName());

    private static final int CHUNK_SIZE = 10000;

    private Socket socket;
    private volatile boolean stop;
    private int senderNumber;
    private static volatile Integer sendersCreated = 0;

    private MicrophoneReader mr;

    Sender(Socket socket) throws LineUnavailableException {
        this.socket = socket;
        stop = false;
        senderNumber = ++sendersCreated;
        LOG.log(Level.INFO, "Sender started: {0}", senderNumber);

        mr = MicrophoneReader.getInstance();
        if (!mr.init()) {
            throw new LineUnavailableException("Cannot init MicrophoneReader");
        }
    }

    void setStop() {
        stop = true;
    }

    @Override
    public void run() {
        LOG.info("Sender started");
        try (OutputStream os = socket.getOutputStream()) {

            while (!stop) {
                byte[] data = new byte[CHUNK_SIZE];
                int numBytesRead = mr.getMicrophone().read(data, 0, CHUNK_SIZE);

                os.write(data, 0, numBytesRead);
                os.flush();

                LOG.log(Level.CONFIG, "Sender {0}: {1} bytes sent", new Object[]{senderNumber, numBytesRead});
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
