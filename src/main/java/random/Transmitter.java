package random;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Transmitter {

    private static int PORT = 7373;

    private static List<Sender> senderList = new ArrayList<>();

    public static String STOP_WORD = "stop";

    public static void transmit(String param) {

        if (STOP_WORD.equals(param)) {
            stopTransmitter();
            return;
        }

        try {
            ServerSocket ss = new ServerSocket(PORT);

            while (true) {
                Socket s = ss.accept();

                Sender sndr = new Sender(s);
                senderList.add(sndr);
                sndr.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            senderList.forEach(Sender::setStop);
        }
    }

    private static void stopTransmitter() {
        System.out.println("stop");
        String host = "localhost";
        Socket s = null;
        OutputStream os = null;
        try {
            InetAddress ipAddr = InetAddress.getByName(host);
            s = new Socket(ipAddr, PORT);
            os = s.getOutputStream();
            os.write("exit".getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.close(os);
            Utils.close(s);
        }
    }
}
