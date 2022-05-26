import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {
    private static final HashSet<Socket> clients = new HashSet<Socket>();
    private static Socket client;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("Server is listening");
            while (true) {
                UserHandler userHandler = new UserHandler();
                client = serverSocket.accept();
                clients.add(client);
                userHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static HashSet<Socket> getClients() {
        return clients;
    }

    public static Socket getClient() {
        return client;
    }
}
