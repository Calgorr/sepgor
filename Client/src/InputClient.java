import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class InputClient extends Thread {
    private Socket socket;
    private Notif notif;

    public InputClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            while (socket.isConnected()) {
                notif = (Notif) inputStream.readObject();
            }
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
