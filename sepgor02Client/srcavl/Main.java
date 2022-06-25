import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;


public class Main {

    static Content content = new Content("", "", "");
    static Client client;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException, InterruptedException {
        ///load publickey and private
        if (saveContents.load()) {
            System.out.println("loaded keys successfully");
        } else {
            new RSAKeyPairGenerator();
            System.out.println("didnt have privateKey so i generated");

        }
        System.out.println(content.privateKey + "  " + content.publicKey);

        ///load serverKey

        client = new Client();
        client.connect();
        Socket socket = client.socket;
        content.validKey(client.socket, client.inputClient);
        System.out.println(content.serverId + " s " + content.serverKey);
        saveContents.save();
        ;
    }
}