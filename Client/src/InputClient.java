import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class InputClient extends Thread {
    private Socket socket;
    private Message message;

    public InputClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                message = (Message) inputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Conn ended");
            RunChat.serverMessage = message.getMessage();
        }
    }
}
