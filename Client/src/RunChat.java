import java.io.IOException;

public class RunChat {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
        client.connect();
    }
}
