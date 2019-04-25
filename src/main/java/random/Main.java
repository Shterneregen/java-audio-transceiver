package random;

import random.receive.Receiver;
import random.receive.TcpReceiver;
import random.receive.UdpReceiver;
import random.transmit.TcpTransmitter;
import random.transmit.Transmitter;
import random.transmit.UdpTransmitter;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    private static final String PORT_PARAM_NOT_FOUND = "Port param not found!";

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                int index = 0;
                String mode = args[index++];
                String[] params = Arrays.copyOfRange(args, index, args.length);

                if ("-r".equals(mode)) {
                    receiveTcp(params);
                } else if ("-t".equals(mode)) {
                    transmit(params, true);
                } else if ("-ru".equals(mode)) {
                    receiveUdp(params);
                } else if ("-tu".equals(mode)) {
                    transmit(params, false);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void receiveTcp(String[] args) throws UnknownHostException {
        if (args.length < 2) {
            throw new IllegalArgumentException("You should pass host and port params!");
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Receiver receiver = new TcpReceiver(host, port);
        receiver.receive();
    }

    private static void receiveUdp(String[] args) throws UnknownHostException {
        if (args.length < 1) {
            throw new IllegalArgumentException(PORT_PARAM_NOT_FOUND);
        }
        int port = Integer.parseInt(args[0]);
        Receiver receiver = new UdpReceiver(port);
        receiver.receive();
    }

    private static void transmit(String[] args, boolean tcp) {
        if (args.length < 1) {
            throw new IllegalArgumentException(PORT_PARAM_NOT_FOUND);
        }
        int port = Integer.parseInt(args[0]);
        Transmitter transmitter = tcp
                ? new TcpTransmitter(port)
                : new UdpTransmitter(port);
        transmitter.transmit();
    }
}
