import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {
    private static final HashSet<UserHandler> clients = new HashSet<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            while (true) {
                Socket client;
                client = serverSocket.accept();
                UserHandler userHandler = new UserHandler(client);
                clients.add(userHandler);
                userHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static HashSet<UserHandler> getClients() {
        return clients;
    }

}