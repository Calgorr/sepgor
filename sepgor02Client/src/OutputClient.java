import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;

public class OutputClient extends Thread {
    private SeConMessage message;
    private Socket socket;
    private InputClient inputClient;
    private int chatId;
    private String text;

    public OutputClient(Socket socket, InputClient inputClient, SeConMessage message) {
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
                //SeConMessage message1 = new SeConMessage(0,RSAUtil.publicKey);
               // outputStream.writeObject(message1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
