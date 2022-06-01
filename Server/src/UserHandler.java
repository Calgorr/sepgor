import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserHandler extends Thread {
    private Socket client;
    private ObjectOutputStream outputStream;
    private int userId;

    public UserHandler(Socket client) throws IOException {
        this.client = client;
        outputStream = new ObjectOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
            SeConMessage seConMessage;
            // yeki az moshkel ha az inja bood chon socket dasht baste mishod age niaze ke chand ta client response befreste
            // bayad ke if ro tabdil be while bokoni to khat payini
            if (client.isConnected()) {
                seConMessage = (SeConMessage) inputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void notifToClients(Notif notif) throws IOException {
        for (UserHandler other : Server.getClients()) {
            other.outputStream.writeObject(notif);
        }
    }
}
