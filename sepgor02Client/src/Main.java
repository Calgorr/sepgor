import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;


public class Main {

    static Content content = new Content("", "", "","","sepehrmnp","sepehr1381");
    static Client client;
    public static void sendCommand(int command,int chatId,Message message){
        String session = content.session;

        new OutputClient(client.socket, client.inputClient,new SeConMessage(content.serverId,Encrypt.encrypt(new Gson().toJson(new Command(session,command,chatId,message,1)),content.serverKey))).start();

    }
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException, InterruptedException {
        ///load publickey and private
        if (saveContents.load()) {
            System.out.println("loaded keys successfully");
        } else {
            new RSAKeyPairGenerator();
            System.out.println("didnt have privateKey so i generated");

        }
        System.out.println(content.privateKey + "  " + content.publicKey+" serverKey: " +content.serverKey);

        ///load serverKey

        client = new Client();
        client.connect();
        Socket socket = client.socket;
        content.validKey(client.socket, client.inputClient);
        content.validSession(client.socket,client.inputClient,"sepehrmnp","sepehr1381");


        saveContents.save();
       // sendCommand(0,0,new TextMessage("fff"));
    }
}
