import java.io.IOException;

public class RunChat {
    static String serverMessage;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(send(new Message("Salam", "sepehr"), "127.0.0.1", "6000"));
    }

    public static String send(Message message, String ipAddress, String port) throws IOException, InterruptedException {
        Client client1 = new Client(message, ipAddress, port);
        client1.connect();
        return serverMessage;
    }
}
