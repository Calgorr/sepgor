import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;

public class UserHandler extends Thread {
    private Socket client;
    private HashSet<Socket> clients = new HashSet<>();
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public UserHandler() throws IOException {
    }

    @Override
    public void run() {
        try {
            clients = Server.getClients();
            client = Server.getClient();
            inputStream = new ObjectInputStream(client.getInputStream());
            outputStream = new ObjectOutputStream(client.getOutputStream());
            Message message;
            message = (Message) inputStream.readObject();
            System.out.println(message.getSender() + " : " + message.getMessage());
//            while ((message = (Message) inputStream.readObject()) != null) {
//                if (message.getMessage().equals("#exit")) {
//                    break;
//                }
//                broadcastToClients(message);
//            }
            Message serverMessage = new Message(message.getMessage() + "1", "Server");
            outputStream.writeObject(serverMessage);
            client.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public void broadcastToClients(Message message) {
//        for (Socket other : clients) {
//            if (client == other) {
//                continue;
//            }
//            try {
//                outputStream.writeObject(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
