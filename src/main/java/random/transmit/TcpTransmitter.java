package random.transmit;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpTransmitter implements Transmitter {

    private static final Logger LOG = Logger.getLogger(TcpTransmitter.class.getName());
    private static final String STOP_WORD = "stop";
    private static final List<Sender> senderList = new ArrayList<>();

    private final int port;

    private boolean stop = false;

    public TcpTransmitter(int port) {
        this.port = port;
    }

    @Override
    public void transmit() {

//        if (STOP_WORD.equals(param)) {
//            stopTransmitter();
//            return;
//        }

        try (ServerSocket ss = new ServerSocket(port)) {

            while (!stop) {
                Socket socket = ss.accept();

                Sender sndr = new Sender(socket);
                senderList.add(sndr);
                sndr.start();
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            senderList.forEach(Sender::setStop);
        }
    }

    public void stopTransmitter(int port) throws UnknownHostException {
        LOG.info("Transmitter was stopped");
        String host = "localhost";
        InetAddress ipAddr = InetAddress.getByName(host);
        try (Socket socket = new Socket(ipAddr, port);
             OutputStream os = socket.getOutputStream()) {
            os.write("exit".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
