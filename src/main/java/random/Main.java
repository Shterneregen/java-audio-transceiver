package random;

import random.receive.Receiver;
import random.receive.TcpReceiver;
import random.receive.UdpReceiver;
import random.transmit.TcpTransmitter;
import random.transmit.Transmitter;
import random.transmit.UdpTransmitter;

import java.lang.invoke.MethodHandles;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
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
                    transmitTcp(params);
                } else if ("-ru".equals(mode)) {
                    receiveUdp(params);
                } else if ("-tu".equals(mode)) {
                    transmitUdp(params);
                }
            } else {
                help();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            help();
        }
    }

    private static void receiveTcp(String[] args) throws UnknownHostException {
        validateParams(args, 2, "You should pass host and port params!");
        int port = Integer.parseInt(args[0]);
        String host = args[1];
        Receiver receiver = new TcpReceiver(host, port);
        receiver.receive();
    }

    private static void receiveUdp(String[] args) throws UnknownHostException {
        validateParams(args, 1, PORT_PARAM_NOT_FOUND);
        int port = Integer.parseInt(args[0]);
        Receiver receiver = new UdpReceiver(port);
        receiver.receive();
    }

    private static void transmitTcp(String[] args) {
        validateParams(args, 1, PORT_PARAM_NOT_FOUND);
        int port = Integer.parseInt(args[0]);
        Transmitter transmitter = new TcpTransmitter(port);
        transmitter.transmit();
    }

    private static void transmitUdp(String[] args) {
        if (args.length < 1 || args.length > 2) {
            throw new IllegalArgumentException();
        }
        int port = Integer.parseInt(args[0]);
        String destinationIpAddress = args.length > 1 ? args[1] : "localhost";
        Transmitter transmitter = new UdpTransmitter(port, destinationIpAddress);
        transmitter.transmit();
    }

    private static void validateParams(String[] args, int validParamNumber, String validateMsg) {
        if (args.length < validParamNumber) {
            throw new IllegalArgumentException(validateMsg);
        }
    }

    private static void help() {
        String help = "====================================================\n" +
                "USAGE\n" +
                "====================================================\n" +
                "TCP Transmitter\n" +
                "\tjava -jar audio.jar -t TRANSMITTER_PORT\n\n" +
                "TCP Receiver\n" +
                "\tjava -jar audio.jar -r TRANSMITTER_PORT TRANSMITTER_IP\n\n" +
                "UDP Transmitter (if RECEIVER_IP is not present localhost will be used)\n" +
                "\tjava -jar audio.jar -tu TRANSMITTER_PORT\n" +
                "\tjava -jar audio.jar -tu TRANSMITTER_PORT RECEIVER_IP\n\n" +
                "UDP Receiver\n" +
                "\tjava -jar audio.jar -ru TRANSMITTER_PORT\n";
        System.out.println(help);
    }
}
