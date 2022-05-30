import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class InputClient extends Thread {
    private Socket socket;
    private ServerReturner serverReturner;

    public InputClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            while (socket.isConnected()) {
                serverReturner = (ServerReturner) inputStream.readObject();
                if(serverReturner.getId()==0){
                    String[] str = serverReturner.getServerKey().split(",");
                    Main.content.serverId = Integer.parseInt(str[0]);
                    Main.content.serverKey = str[1];
                    System.out.println("new keys received");
                }
                else if(serverReturner.getId()==1){
                    if(serverReturner.getText().equals("false")){
                        System.out.println("your key is invalid lets erase this one and ask for another");
                        Main.content.serverKey = "";
                        Main.content.serverId = 0;
                        Main.content.validKey(socket);
                    }else System.out.println("your key is valid");

                }else{
                    System.out.println("sss");
                }
                //serverReturner.t
               // System.out.println(serverReturner.getServerKey());
            }
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
