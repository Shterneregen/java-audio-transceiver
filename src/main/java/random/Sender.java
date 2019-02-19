package random;

import javax.sound.sampled.LineUnavailableException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Sender extends Thread {

    private Socket socket;
    private volatile boolean stop;
    private int senderNumber;
    private static volatile Integer sendersCreated = 0;

    private MicrophoneReader mr;
    private static int CHUNK_SIZE = 10000;

    Sender(Socket socket) throws LineUnavailableException {
        this.socket = socket;
        stop = false;
        System.out.print("Sender started: #");
        senderNumber = ++sendersCreated;
        System.out.println(senderNumber);

        mr = MicrophoneReader.getInstance();
        if (!mr.init()) throw new LineUnavailableException("Cannot init MicrophoneReader");
    }

    void setStop() {
        stop = true;
    }

    public void run() {
        System.out.println("Sender started");
        try (OutputStream os = socket.getOutputStream();
             InputStream is = socket.getInputStream()) {

            while (!stop) {
                byte[] data = new byte[CHUNK_SIZE];
                Integer numBytesRead = mr.getMicrophone().read(data, 0, CHUNK_SIZE);

                os.write(data, 0, numBytesRead);
                os.flush();

                System.out.print("Sender #");
                System.out.print(senderNumber);
                System.out.print(": ");
                System.out.print(numBytesRead);
                System.out.println(" bytes sent");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
