package pkg;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static SeConMessage message;
    public Socket socket;
    public InputClient inputClient;

    public void connect() throws IOException, InterruptedException {
        socket = new Socket("127.0.0.1", 6009);
        inputClient = new InputClient(socket);
//        OutputClient outputClient = new OutputClient(socket, inputClient, message);
//        outputClient.start();
        inputClient.start();


    }
}
