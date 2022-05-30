import java.io.IOException;
import java.net.Socket;

public class Client {
    private Notif message;

    public void connect() {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 6000);
        } catch (IOException e) {
            System.out.println("Problem with connceting");
        }
        InputClient inputClient = new InputClient(socket);
        inputClient.start();
    }
}
