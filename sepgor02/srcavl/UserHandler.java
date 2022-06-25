import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class UserHandler extends Thread {
    private Socket client;
    private ObjectOutputStream outputStream;
    private int userId;

    public UserHandler(Socket client) throws IOException {
        this.client = client;
        outputStream = new ObjectOutputStream(client.getOutputStream());
    }



    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
            SeConMessage seConMessage;
            if (client.isConnected()) {
                seConMessage = (SeConMessage) inputStream.readObject();
                if(seConMessage.getChatId()==0){
                    String publicKey = seConMessage.getText();
                    ServerReturner returner = new ServerReturner(0,Secon.createSecon(publicKey));
                    outputStream.writeObject(returner);
                    System.out.println(returner.getText());
                }else if(seConMessage.getChatId()>0){
                    if(seConMessage.getText().equals("1")){
                        if(Main.dataManagement.IdIsValid(seConMessage.getChatId())){
                            ServerReturner returner = new ServerReturner(1,"true");
                            outputStream.writeObject(returner);
                        } else{
                            ServerReturner returner = new ServerReturner(1,"false");
                            outputStream.writeObject(returner);
                        }
                    }

                }else{
                    System.out.println("message not supported!");
                }
            }
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public synchronized void notifToClients(Notif notif) throws IOException {
        for (UserHandler other : Server.getClients()) {
            other.outputStream.writeObject(notif);
        }
    }
}