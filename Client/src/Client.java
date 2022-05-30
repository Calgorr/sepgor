import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Message message;

    public void connect() throws IOException, InterruptedException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your name");
        String name = input.nextLine();
        Socket socket = new Socket("127.0.0.1", 6000);
        message = new Message("null", name, socket.getLocalPort());
        System.out.println("Connected");
        InputClient inputClient = new InputClient(socket);
        OutputClient outputClient = new OutputClient(socket, inputClient, message);
        outputClient.start();
        inputClient.start();
    }
}
