package random.receive;

import javax.sound.sampled.AudioFormat;
import java.net.UnknownHostException;

public interface Receiver {

    void receive(AudioFormat format8000) throws UnknownHostException;
}
