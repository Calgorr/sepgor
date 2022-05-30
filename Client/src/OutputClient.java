import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class OutputClient extends Thread {
    private Message message;
    private Socket socket;
    private InputClient inputClient;

    public OutputClient(Socket socket, InputClient inputClient, Message message) {
        this.message = message;
        this.socket = socket;
        this.inputClient = inputClient;
    }

    @Override
    public void run() {
        Scanner input = new Scanner(System.in);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (socket.isConnected()) {
                Message message1 = new Message(input.nextLine(), message.getSender(), socket.getLocalPort());
                outputStream.writeObject(message1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
