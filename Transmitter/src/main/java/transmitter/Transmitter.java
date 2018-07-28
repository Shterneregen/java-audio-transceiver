package transmitter;

import javax.sound.sampled.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Transmitter {

    private static List<Sender> senderList = new ArrayList<>();
    private static MicrophoneReader mr;
    private static volatile Integer sendersCreated = 0;
    private static volatile Integer numBytesRead;
    private static volatile Integer senderNotReady = 0;
    private static volatile byte[] data;
    private static final Object monitor = new Object();

    public static void main(String[] args) {

        if (args.length > 0) {
            String arg = args[0];
            if ("stop".equals(arg)) {
                System.out.println("hi");
                String host = "localhost";
                InetAddress ipAddr = null;
                Socket s = null;
                OutputStream os = null;
                try {
                    ipAddr = InetAddress.getByName(host);
                    s = new Socket(ipAddr, 7373);
//                    InputStream is = s.getInputStream();
                    os = s.getOutputStream();
                    os.write("exit".getBytes(Charset.forName("UTF-8")));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (os != null) {
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (s != null) {
                            s.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }

        try {
            mr = new MicrophoneReader();
            mr.start();

            ServerSocket ss = new ServerSocket(7373);

//            Scanner sc = new Scanner(System.in);

            //while (!sc.next().equals("quit")) {
            while (true) {
                Socket s = ss.accept();

                Sender sndr = new Sender(s);
                senderList.add(sndr);
                sndr.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mr.setFinishFlag();
            senderList.forEach(Sender::setFinishFlag);
        }
    }

    static class Sender extends Thread {

        Socket s;
        volatile boolean finishFlag;
        int position;
        int senderNumber;

        Sender(Socket s) {
            this.s = s;
            finishFlag = false;
            System.out.print("Sender started: #");
            senderNumber = ++sendersCreated;
            System.out.println(senderNumber);
        }

        void setFinishFlag() {
            finishFlag = true;
        }

        public void run() {
            try (OutputStream os = s.getOutputStream(); InputStream is = s.getInputStream();) {
//                OutputStream os = s.getOutputStream();
//                InputStream is = s.getInputStream();

                while (!finishFlag) {
                    synchronized (monitor) {
                        senderNotReady++;

                        monitor.wait();
                        String result = null;
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                            result = br.lines().collect(Collectors.joining("\n"));
                        } catch (Exception e) {
                            System.exit(0);
//                            break;
                        }
                        if ("exit".equals(result)) {
                            setFinishFlag();
                            System.out.println("exit");
                            System.exit(0);
//                            break;
                        }
//                        if (finishFlag) break;

                        os.write(data, 0, numBytesRead);
                        os.flush();
                        senderNotReady--;
                    }
//                    if (finishFlag) break;
                    System.out.print("Sender #");
                    System.out.print(senderNumber);
                    System.out.print(": ");
                    System.out.print(numBytesRead);
                    System.out.println(" bytes sent");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class MicrophoneReader extends Thread {

        volatile boolean finishFlag;

        //         AudioFormat format = new AudioFormat(16000.0f, 16, 2, true, false);
        AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, false);
        int CHUNK_SIZE = 10000;
        TargetDataLine microphone;

        MicrophoneReader() {
            finishFlag = false;
            System.out.println("Microphone reader started");
        }

        void setFinishFlag() {
            finishFlag = true;
        }

        public void run() {
            try {
                microphone = AudioSystem.getTargetDataLine(format);

                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);

                data = new byte[CHUNK_SIZE];
                microphone.start();

                while (!finishFlag) {
                    synchronized (monitor) {
                        if (senderNotReady.equals(sendersCreated)) {
                            monitor.notifyAll();
                            continue;
                        }
                        numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                    }

                    System.out.print("Microphone reader: ");
                    System.out.print(numBytesRead);
                    System.out.println(" bytes read");
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}
