package random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Receiver {

    private static int PORT = 7373;
    private static int CHUNK_SIZE = 10000;

    public static void receive(String host) {
        // AudioFormat format = new AudioFormat(16000.0f, 16, 2, true, false);
        AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, false);
        SourceDataLine speakers;

        try {
            InetAddress ipAddr = InetAddress.getByName(host);

            Socket socket = new Socket(ipAddr, PORT);
            InputStream is = socket.getInputStream();

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();

            int numBytesRead;

            byte[] data = new byte[CHUNK_SIZE];

            while (true) {
                numBytesRead = is.read(data);
                speakers.write(data, 0, numBytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
