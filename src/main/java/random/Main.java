package random;

import java.util.Arrays;

public class Main {
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
            e.printStackTrace();
        }
    }

    private static void receive(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        Receiver.receive(host);
    }

    private static void transmit(String[] args) {
        String param = args.length > 0 ? args[0] : "";
        Transmitter.transmit(param);
    }
}
