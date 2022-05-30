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
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            while (socket.isConnected()) {
                message = (Message) inputStream.readObject();
                System.out.println(message.getSender() + " : " + message.getMessage());
            }
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
