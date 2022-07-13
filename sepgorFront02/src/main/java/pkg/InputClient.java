package pkg;

import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class InputClient extends Thread {
    /**
     * inputClient for every client
     */
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
                if(serverReturner.getId()==-1){
                    String[] str = serverReturner.getServerKey2().split(",");
                    Main.content.serverId = Integer.parseInt(str[0]);
                    Main.content.serverKey = str[1];
                    System.out.println("new keys received");
                    System.out.println("server id: "+Main.content.serverId+ "   server key:"+Main.content.serverKey);

                }
                else if(serverReturner.getId()==0){
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
                        System.out.println("login unsuccessful");
                        Main.controller.alertUnsuccessful();
                    }else{

                        System.out.println("login successful");
                        Main.content.session =serverReturner.getText();

                        Main.content.username = Menu.tmpUsername;
                        Main.content.password = Menu.tmpPassword;
                        Menu.signIn(Main.content.username,Main.content.password);
                        Main.controller.showHome(Main.content.username);
                        System.out.println("tst38 "+Main.content.username+" "+Main.content.password);


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
                }else if(serverReturner.getId()==4){
                    //System.out.println(serverReturner.getText());
                    String[] userPub = serverReturner.getText().split("@");
                    Main.userNameToChat = userPub[0];
                    Main.publicKeyToChat = userPub[1];
                  //  System.out.println(Main.userNameToChat + "  "+ Main.publicKeyToChat);
                }else if(serverReturner.getId()==5){
                    System.out.println(serverReturner.getText());
                }else if(serverReturner.getId()==6){

                    System.out.println(Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey));
                }else if(serverReturner.getId()==7){
                    if(!Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey).equals("")){
                    String[] pendings = Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey).split("&")[0].split(",");
                    Main.controller.clearPendingVBox();
                    if (!pendings[0].equals("")) {
                        for (int i = 0; i < pendings.length; i++) {
                            Main.controller.addFriendsToPending(pendings[i].split(":")[0], pendings[i].split(":")[1]);
                        }
                    }
                    String[] pendingsSent = Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey).split("&")[1].split(",");
                    if (!pendingsSent[0].equals("")) {
                        for (int i = 0; i < pendingsSent.length; i++) {
                            System.out.println("tst37"+pendingsSent[i].split(":")[0]);
                            Main.controller.addFriendsToPending2(pendingsSent[i].split(":")[0], pendingsSent[i].split(":")[1]);
                        }
                    }}else{ Main.controller.clearPendingVBox();}

                }else if(serverReturner.getId()==8){

                    String[] pubPrv = Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey).split(":");
                    Main.content.publicKey=pubPrv[0];
                    Main.content.privateKey = Encrypt.decrypt(pubPrv[1],Main.content.password);
                    System.out.println(Main.content.privateKey);

                }else if(serverReturner.getId()==9){
                    String servers = Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey);
                    System.out.println("tst16"+ servers);
                    String[] serversIds = servers.split(",");
                    if(!serversIds[0].equals("")){
                    for (int i = 0; i < serversIds.length; i++) {
                        Main.controller.addChatToChats(serversIds[i].split(":")[0],serversIds[i].split(":")[1]);
                    }
                    }
                }else if(serverReturner.getId()==10){
                    String[] messages = Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey).split(";");
                    while (true){
                        Thread.sleep(500);
                        if(!Main.chatEncryption.equals("")){
                            break;
                        }
                    }
                    Main.controller.clearChat();
                   try{
                       for (int i = 0; i < messages.length; i++) {
                           System.out.println(messages[i]);

                           //System.out.println(new Gson().fromJson(messages[i],ClientMessage.class).message.toString());
                           System.out.println(Encrypt.decrypt((new Gson().fromJson((new Gson().fromJson(messages[i],ClientMessage.class).message),TextMessage.class)).getText(),Main.chatEncryption));
                           ClientMessage msg =(new Gson().fromJson(messages[i], ClientMessage.class));
                           if(!(msg.replyId>0)) {
                               if(msg.MessageType==3){
                                   Main.controller.addMessageToChatT3(msg.messageId, String.valueOf(msg.senderId),((new Gson().fromJson(msg.message, TextMessage.class)).getText()), msg.date, "likes: " + msg.like + "   disLikes: " + msg.disliek + "   laughs: " + msg.laugh);
                               }
                               else {
                                   Main.controller.addMessageToChat(msg.messageId, String.valueOf(msg.senderId), Encrypt.decrypt((new Gson().fromJson(msg.message, TextMessage.class)).getText(), Main.chatEncryption), msg.date, "likes: " + msg.like + "   disLikes: " + msg.disliek + "   laughs: " + msg.laugh);
                               }
                           }else{
                               String reply = "";
                               for (int j = 0; j < messages.length; j++) {
                                   ClientMessage msg2 =(new Gson().fromJson(messages[j], ClientMessage.class));
                                   if(msg2.messageId==msg.replyId){
                                       ///del
                                       System.out.println("tst12");
                                       reply = Encrypt.decrypt((new Gson().fromJson(msg2.message, TextMessage.class)).getText(), Main.chatEncryption);
                                       if(reply.length()>102) reply = reply.substring(100);
                                       break;
                                   }
                               }
                               Main.controller.addMessageToChatT2(msg.messageId, String.valueOf(msg.senderId), Encrypt.decrypt((new Gson().fromJson(msg.message, TextMessage.class)).getText(), Main.chatEncryption), msg.date,reply,"likes: "+msg.like+ "   disLikes: "+msg.disliek+"   laughs: "+msg.laugh);
                           }
                       }
                   }catch (NullPointerException e){
                       System.out.println("no messages yet!");
                   }

                    Main.endShow = true;
                }else if(serverReturner.getId()==11){
                    Main.controller.clearChannels();
                    String servers = Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey);
                    System.out.println(servers);
                    String[] serversIds = servers.split(",");
                    for (int i = 0; i < serversIds.length; i++) {
                        Main.controller.addChannelsToChannelsVBox(serversIds[i].split(":")[0],serversIds[i].split(":")[1]);
                    }
                }else if(serverReturner.getId()==12){
                    TextMessage encryption = new Gson().fromJson(Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey),TextMessage.class);
                    Main.chatEncryption = RSAUtil.decrypt(encryption.getText(),Main.content.privateKey);
                    System.out.println("chat encryption is: "+ Main.chatEncryption);
                }else if(serverReturner.getId()==13){
                    String[] friends =Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey).split(",");
                    System.out.println("friends are: \n"+ friends );
                    Main.controller.clearChannels();
                    Main.controller.clearAllVBox();
                    Main.controller.clearOnlineVBox();
                    if (!friends[0].equals("")){
                    for (int i = 0; i < friends.length; i++) {
                        String[] friendData = friends[i].split(":");
                        if(friendData[2].equals("5"))  Main.controller.addFriendsToOnlineVBox(friendData[0],friendData[1]);
                        Main.controller.addFriendsToAll(friendData[0],friendData[1]);
                        Main.controller.addFriendsToChannelsVBox(friendData[0],friendData[1],friendData[2]);
                    }}
                }
                else if(serverReturner.getId()==14){
                    String[] friends =Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey).split(",");
                    Main.controller.clearUsersOfServer();
                    for (int i = 0; i < friends.length; i++) {
                        Main.controller.addUsersToChatUSers(friends[i]);
                    }
                }
                else if(serverReturner.getId()==15){
                    String friends =Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey);
                    System.out.println("pins are: \n"+ friends );
                }else if(serverReturner.getId()==16){
                    String[] blocked = Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey).split(",");
                    Main.controller.clearBlockedVBox();
                    if(!blocked[0].equals("")){
                        for (int i = 0; i < blocked.length; i++) {
                            Main.controller.addBlockedToBlockedVBox(blocked[i].split(":")[0],blocked[i].split(":")[1]);
                        }
                    }

                }else if(serverReturner.getId()==17){
                    System.out.println(Encrypt.decrypt(serverReturner.getText(),Main.content.serverKey));
                }else if(serverReturner.getId()==18){
                    System.out.println("new notif for: "+serverReturner.getText());
                    if(Integer.parseInt(serverReturner.getText())==Main.chatId){
                        Main.showChat(Main.chatId);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
