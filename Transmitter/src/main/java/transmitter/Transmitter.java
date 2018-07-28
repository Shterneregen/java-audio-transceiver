package transmitter;

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
    public static final Object monitor = new Object();

    public static String STOP_WORD = "stop";

    public static void main(String[] args) {

        if (args.length > 0) {
            String arg = args[0];
            if (STOP_WORD.equals(arg)) {
                stopTransmitter();
                return;
            }
        }

        try {
            ServerSocket ss = new ServerSocket(PORT);

//            Scanner sc = new Scanner(System.in);
//            while (!sc.next().equals("quit")) {
            while (true) {
                Socket s = ss.accept();

                Sender sndr = new Sender(s);
                senderList.add(sndr);
                sndr.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            mr.setFinishFlag();
            senderList.forEach(Sender::setFinishFlag);
        }
    }

    private static void stopTransmitter() {
        System.out.println("stop");
        String host = "localhost";
        InetAddress ipAddr = null;
        Socket s = null;
        OutputStream os = null;
        try {
            ipAddr = InetAddress.getByName(host);
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
