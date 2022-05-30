import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;


public class Main {

    static Content content = new Content("","","");

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException, InterruptedException {
        ///load publickey and private
        if(saveContents.load()){
           System.out.println("loaded keys successfully");
        } else{
            new RSAKeyPairGenerator();
            System.out.println("didnt have privateKey so i generated");

        }
        System.out.println(content.privateKey+"  "+ content.publicKey);

        ///load serverKey

        Client client = new Client();
        client.connect();
        content.validKey(client.socket);
        System.out.println(content.serverId+" s "+ content.serverKey);
        saveContents.save();;
    }
}
