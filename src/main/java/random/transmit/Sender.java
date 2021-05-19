package random.transmit;

import random.audio.MicrophoneReader;

import javax.sound.sampled.TargetDataLine;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender extends Thread {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final int CHUNK_SIZE = 256;
    private static final AtomicInteger sendersCount = new AtomicInteger(0);

    private final Socket socket;
    private final int senderNumber;

    private volatile boolean stop = false;

    Sender(Socket socket) {
        this.socket = socket;
        senderNumber = sendersCount.incrementAndGet();
        LOG.log(Level.INFO, "Sender started: {0}", sendersCount);
    }

    void setStop() {
        stop = true;
    }

    @Override
    public void run() {
        LOG.info("Sender started");

        try (OutputStream os = socket.getOutputStream();
             TargetDataLine microphone = MicrophoneReader.getMicrophone()) {

            while (!stop) {
                byte[] data = new byte[CHUNK_SIZE];
                int numBytesRead = microphone.read(data, 0, CHUNK_SIZE);

                os.write(data, 0, numBytesRead);
                os.flush();

                LOG.log(Level.CONFIG, "Sender {0}: {1} bytes sent", new Object[]{senderNumber, numBytesRead});
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
