package transmitter;

import javax.sound.sampled.LineUnavailableException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Sender extends Thread {

    private Socket s;
    private volatile boolean finishFlag;
    //    int position;
    private int senderNumber;
    private static volatile Integer sendersCreated = 0;

    private MicrophoneReader mr;
    private static int CHUNK_SIZE = 10000;

    Sender(Socket s) throws LineUnavailableException {
        this.s = s;
        finishFlag = false;
        System.out.print("Sender started: #");
        senderNumber = ++sendersCreated;
        System.out.println(senderNumber);

//        data = new byte[CHUNK_SIZE];
        mr = MicrophoneReader.getInstance();
        if (!mr.init()) throw new LineUnavailableException("Cannot init MicrophoneReader");
    }

    void setFinishFlag() {
        finishFlag = true;
    }

    public void run() {
        System.out.println("Sender started");
//            try (OutputStream os = s.getOutputStream(); InputStream is = s.getInputStream();) {
        OutputStream os = null;
        InputStream is = null;
        try {
            os = s.getOutputStream();
            is = s.getInputStream();

            while (!finishFlag) {
//                synchronized (Transmitter.monitor) {
//                    senderNotReady++;

//                    Transmitter.monitor.wait();
//                String result = null;
//                try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
//                    result = br.lines().collect(Collectors.joining("\n"));
//                } catch (Exception e) {
//                    Utils.close(os);
//                    Utils.close(is);
////                    System.exit(0);
//                }
//                if (Transmitter.STOP_WORD.equals(result)) {
////                            setFinishFlag();
//                    System.out.println("exit");
//                    Utils.close(os);
//                    Utils.close(is);
//                    System.exit(0);
//                }
//                        if (finishFlag) break;
                byte[] data = new byte[CHUNK_SIZE];
                Integer numBytesRead = mr.getMicrophone().read(data, 0, CHUNK_SIZE);

                os.write(data, 0, numBytesRead);
                os.flush();
//                    senderNotReady--;

                System.out.print("Sender #");
                System.out.print(senderNumber);
                System.out.print(": ");
                System.out.print(numBytesRead);
                System.out.println(" bytes sent");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.close(os);
            Utils.close(is);
        }
    }
}
