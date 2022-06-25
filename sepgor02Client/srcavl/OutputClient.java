import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OutputClient extends Thread {
    private SeConMessage message;
    private Socket socket;
    private InputClient inputClient;

    public OutputClient(Socket socket, InputClient inputClient, SeConMessage message) {
        this.message = message;
        this.socket = socket;
        this.inputClient = inputClient;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (socket.isConnected()) {
                outputStream.writeObject(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}