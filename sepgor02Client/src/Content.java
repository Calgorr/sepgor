import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

public class Content implements Serializable {
    String publicKey = ""; //= Base64.getEncoder().encodeToString(keyPair.getPublicKey().getEncoded());//
    String privateKey = "";//=Base64.getEncoder().encodeToString(keyPair.getPrivateKey().getEncoded()); //
    String serverKey = "";
    String session = "";
    private static Socket socket;
    int serverId = 0;
    String username ="";
    String password = "";


    public Content(String publicKey, String privateKey, String serverKey, String session,String username, String password) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.serverKey = serverKey;
        this.session = session;
        this.username = username;
        this.password = password;
    }

    public void validKey(Socket socket, InputClient inputClient) {

        if (!serverKey.equals("")) {
            System.out.println("you have a key, lets see if its valid");
            new OutputClient(socket, inputClient, new SeConMessage(this.serverId, "1")).start();
        } else {
            System.out.println("you dont have a key lets get one!");
            /*try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                if (socket.isConnected()) {
            SeConMessage message1 = new SeConMessage(0,RSAUtil.publicKey);
            outputStream.writeObject(message1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }*/
           //System.out.println(Main.content.publicKey);
            SeConMessage message = new SeConMessage(0, Main.content.publicKey);
            //System.out.println(message.getText());
            OutputClient outputClient = new OutputClient(socket, inputClient, message);
            outputClient.start();
        }
    }

    public void validSession(Socket socket, InputClient inputClient,String userName,String password) throws NoSuchAlgorithmException {

        if (!session.equals("")) {
            System.out.println("you have a session, lets see if its valid");
            Main.sendCommand(1,0,new TextMessage(session));
        } else {
            System.out.println("you dont have a session lets get one!");
            Main.sendCommand(0,0,new TextMessage(userName+"-"+Functions.sha3(password)));
            /*try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                if (socket.isConnected()) {
            SeConMessage message1 = new SeConMessage(0,RSAUtil.publicKey);
            outputStream.writeObject(message1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(Main.content.publicKey);

            SeConMessage message = new SeConMessage(0, Main.content.publicKey);
            System.out.println(message.getText());
            OutputClient outputClient = new OutputClient(socket, inputClient, message);
            outputClient.start();*/
        }
    }

}
