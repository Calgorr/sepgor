import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class OutputClient extends Thread {
    private Message message ;
    private Socket socket;
    private InputClient inputClient;

    public OutputClient(Socket socket, InputClient inputClient, Message message) {
        this.message = message;
        this.socket = socket;
        this.inputClient = inputClient;
    }

    @Override
    public void run() {
        try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(message);
        } catch (IOException e) {

        }

    }
}
