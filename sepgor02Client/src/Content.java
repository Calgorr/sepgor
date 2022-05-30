import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Content implements Serializable {
     String publicKey = ""; //= Base64.getEncoder().encodeToString(keyPair.getPublicKey().getEncoded());//
     String privateKey = "";//=Base64.getEncoder().encodeToString(keyPair.getPrivateKey().getEncoded()); //
     String serverKey ="";
    private static Socket socket;
     int serverId = 0;

    public Content(String publicKey, String privateKey, String serverKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.serverKey = serverKey;
    }

    public void validKey(Socket socket){

        if(!serverKey.equals("")){
        try {
            System.out.println("you have a key, lets see if its valid");
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            if (socket.isConnected()) {
                SeConMessage message1 = new SeConMessage(this.serverId, "1");
                outputStream.writeObject(message1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        }else{
            System.out.println("you dont have a key lets get one!");
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                if (socket.isConnected()) {
            SeConMessage message1 = new SeConMessage(0,RSAUtil.publicKey);
            outputStream.writeObject(message1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
