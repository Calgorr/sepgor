import com.google.gson.Gson;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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
        while(client.isConnected()){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
            SeConMessage seConMessage;
            if (client.isConnected()) {
                seConMessage = (SeConMessage) inputStream.readObject();
                ///del
                System.out.println(seConMessage.getText());
                if(seConMessage.getChatId()==0){
                    String publicKey = seConMessage.getText();
                    ServerReturner returner = new ServerReturner(0,Secon.createSecon(publicKey));
                    outputStream.writeObject(returner);
                    System.out.println(returner.getText());
                }else if(seConMessage.getChatId()>0){

                    if(seConMessage.getText().equals("1")){
                        System.out.println("pp");
                       if(Main.dataManagement.IdIsValid(seConMessage.getChatId())){
                           ServerReturner returner = new ServerReturner(1,"true");
                           outputStream.writeObject(returner);
                       } else{
                           ServerReturner returner = new ServerReturner(1,"false");
                           outputStream.writeObject(returner);
                       }
                    }else{
                        String serverGot = Encrypt.decrypt(seConMessage.getText(),Main.dataManagement.getSecCon(seConMessage.getChatId()));
                        ///del
                        System.out.println(serverGot);
                        Command command = new Gson().fromJson(serverGot,Command.class);
                        System.out.println(command.getCommand());
                        int commandId = command.getCommand();
                        switch (commandId){
                            case 0:
                                String[] userPass = ((TextMessage)command.getMessage()).getText().split("-");
                                ///inja
                                System.out.println(userPass[0]+" "+userPass[1]);
                                if(userPass[1].equals(Main.dataManagement.getPassword(userPass[0]))){
                                    System.out.println("login Successful");
                                    outputStream.writeObject(new ServerReturner(2,Main.dataManagement.insertSession(Main.dataManagement.getIdfromUsers(userPass[0]),Functions.createSession())));
                                }else{
                                    System.out.println("login Unsuccessful");
                                    outputStream.writeObject(new ServerReturner(2,"false"));
                                }

                                break;
                            case 1:
                                String session = ((TextMessage)command.getMessage()).getText();
                                outputStream.writeObject(new ServerReturner(3,Main.dataManagement.getUser(Main.dataManagement.getId(session)).getUserName()));
                        }

                    }


                }else{
                    System.out.println("message not supported!");
                }
            }
//            client.close();
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
    }

    public synchronized void notifToClients(Notif notif) throws IOException {
        for (UserHandler other : Server.getClients()) {
            other.outputStream.writeObject(notif);
        }
    }
}
