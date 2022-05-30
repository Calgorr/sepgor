import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OutputThread implements Runnable {

    private Message message;

    public OutputThread(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        Socket socket;
        ObjectOutputStream outputStream = null;
        try {
            socket = new Socket("127.0.0.1", 6000);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
