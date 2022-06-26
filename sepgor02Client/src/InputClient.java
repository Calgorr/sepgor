import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

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
                    saveContents.save();
                }
                else if(serverReturner.getId()==1){
                    if(serverReturner.getText().equals("false")){
                        System.out.println("your key is invalid lets erase this one and ask for another");
                        Main.content.serverKey = "";
                        Main.content.serverId = 0;
                        Main.content.validKey(socket,Main.client.inputClient);
                    }else System.out.println("your key is valid");

                }else if(serverReturner.getId()==2){
                    if(serverReturner.getText().equals("false")){
                        System.out.println("your login was unsuccessful");
                        Scanner sc = new Scanner(System.in);
                         Main.content.validSession(socket,Main.client.inputClient,sc.nextLine(),sc.nextLine());
                    }else{
                        Main.content.session =serverReturner.getText();
                        System.out.println(Main.content.session);
                        saveContents.save();
                    }

                }else if(serverReturner.getId()==3){//kolan in bakhsho taghiir bede va aval username ham ba isession pass bede va to server bgo faghat dotaro check kne va true ya false bede
                    if(Main.content.username.equals(serverReturner.getText())){
                        System.out.println("session is valid");
                    }else{
                        System.out.println("invalid session");
                        Main.content.session="";
                        saveContents.save();

                        Main.content.validSession(socket,Main.client.inputClient,Main.content.username,Main.content.password);
                    }
                }
                else{
                    System.out.println("unKnown format, Update i guess!");
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
