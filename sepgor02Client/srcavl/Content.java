import java.io.Serializable;
import java.net.Socket;

public class Content implements Serializable {
    private static Socket socket;
    String publicKey = ""; //= Base64.getEncoder().encodeToString(keyPair.getPublicKey().getEncoded());//
    String privateKey = "";//=Base64.getEncoder().encodeToString(keyPair.getPrivateKey().getEncoded()); //
    String serverKey = "";
    int serverId = 0;

    public Content(String publicKey, String privateKey, String serverKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.serverKey = serverKey;
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
            System.out.println(Main.content.publicKey);
            SeConMessage message = new SeConMessage(0, Main.content.publicKey);
            OutputClient outputClient = new OutputClient(socket, inputClient, message);
            outputClient.start();
        }
    }

}