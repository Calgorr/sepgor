import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Message message;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String ipAddress;
    private String port;

    public Client(Message message, String ipAddress, String port) {
        this.message = message;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void connect() throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 6000);
        System.out.println("Connected");
        InputClient inputClient = new InputClient(socket);
        OutputClient outputClient = new OutputClient(socket, inputClient, message);
        outputClient.start();
        inputClient.start();
        inputClient.join();
    }
}
