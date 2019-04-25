package random;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                int index = 0;
                String mode = args[index++];
                String[] params = Arrays.copyOfRange(args, index, args.length);

                if ("-r".equals(mode)) {
                    receive(params);
                } else if ("-t".equals(mode)) {
                    transmit(params);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void receive(String[] args) throws UnknownHostException {
        String host = args.length > 0 ? args[0] : "localhost";
        Receiver.receive(host);
    }

    private static void transmit(String[] args) throws UnknownHostException {
        String param = args.length > 0 ? args[0] : "";
        Transmitter.transmit(param);
    }
}
