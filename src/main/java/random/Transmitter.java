package random;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Transmitter {

    private static final Logger LOG = Logger.getLogger(Transmitter.class.getName());

    private static final int DEFAULT_PORT = 7373;
    private static final String STOP_WORD = "stop";

    private static List<Sender> senderList = new ArrayList<>();

    private static boolean stop = false;

    private Transmitter() {
    }

    public static void transmit(String param) throws UnknownHostException {

        if (STOP_WORD.equals(param)) {
            stopTransmitter();
            return;
        }

        try (ServerSocket ss = new ServerSocket(DEFAULT_PORT)) {

            while (!stop) {
                Socket s = ss.accept();

                Sender sndr = new Sender(s);
                senderList.add(sndr);
                sndr.start();
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            senderList.forEach(Sender::setStop);
        }
    }

    private static void stopTransmitter() throws UnknownHostException {
        LOG.info("stop");
        String host = "localhost";
        InetAddress ipAddr = InetAddress.getByName(host);
        try (Socket socket = new Socket(ipAddr, DEFAULT_PORT);
             OutputStream os = socket.getOutputStream()) {
            os.write("exit".getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
