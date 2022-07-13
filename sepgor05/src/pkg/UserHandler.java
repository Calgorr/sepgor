package pkg;

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

public class UserHandler extends Thread {
    /**
     * a thread used for handling each client in the server
     */
    private Socket client;
    private ObjectOutputStream outputStream;
    private int userId = 0;

    public UserHandler(Socket client) throws IOException {
        this.client = client;
        outputStream = new ObjectOutputStream(client.getOutputStream());
    }
    public void sendNotif(int chatId) throws IOException {
        outputStream.writeObject(new ServerReturner(18,String.valueOf(chatId)));
    }

    @Override
    public void run() {
        while (client.isConnected()) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                SeConMessage seConMessage;
                if (client.isConnected()) {
                    seConMessage = (SeConMessage) inputStream.readObject();
                    ///del
                    System.out.println(seConMessage.getText());
                    if (seConMessage.getChatId() == -2) {
                        String[] seconDatas = seConMessage.getText().split(":");
                        int seconId = Integer.parseInt(seconDatas[0]);
                        String[] signUpDatas = Encrypt.decrypt(seconDatas[1], Main.dataManagement.getSecCon(seconId)).split(":");
                        Main.dataManagement.insertUser(signUpDatas[1], signUpDatas[2], signUpDatas[0], signUpDatas[3], signUpDatas[4]);
                    } else if (seConMessage.getChatId() == -1) {
                        String publicKey = seConMessage.getText();
                        ServerReturner returner = new ServerReturner(-1, Secon.createSecon(publicKey));
                        outputStream.writeObject(returner);
                        System.out.println(returner.getText());
                    } else if (seConMessage.getChatId() == 0) {
                        String publicKey = seConMessage.getText();
                        ServerReturner returner = new ServerReturner(0, Secon.createSecon(publicKey));
                        outputStream.writeObject(returner);
                        System.out.println(returner.getText());
                    } else if (seConMessage.getChatId() > 0) {

                        if (seConMessage.getText().equals("1")) {

                            if (Main.dataManagement.IdIsValid(seConMessage.getChatId())) {
                                ServerReturner returner = new ServerReturner(1, "true");
                                outputStream.writeObject(returner);
                            } else {
                                ServerReturner returner = new ServerReturner(1, "false");
                                outputStream.writeObject(returner);
                            }
                        } else {
                            String serverGot = Encrypt.decrypt(seConMessage.getText(), Main.dataManagement.getSecCon(seConMessage.getChatId()));

                            Command command = new Gson().fromJson(serverGot, Command.class);
                            System.out.println(command.getCommand());
                            int commandId = command.getCommand();
                            int id = Main.dataManagement.getId(command.getSession());
                            if (Main.dataManagement.getStats(id) == 4 || Main.dataManagement.getStats(id) == 5) {
                                Main.dataManagement.changeStats(id, 5);
                                userId = id;
                                Server.connectedClients.put(id,this);

                            }
                            ///del
                            System.out.println(id);
                            switch (commandId) {
                                case 0:
                                    System.out.println("hi");
                                    String[] userPass = ((TextMessage) command.getMessage()).getText().split("-");
                                    ///inja
                                    System.out.println(userPass[0] + " " + userPass[1]);
                                    if (userPass[1].equals(Main.dataManagement.getPassword(userPass[0]))) {
                                        System.out.println("login Successful");
                                        outputStream.writeObject(new ServerReturner(2, Main.dataManagement.insertSession(Main.dataManagement.getIdfromUsers(userPass[0]), Functions.createSession())));
                                    } else {
                                        System.out.println("login Unsuccessful");
                                        outputStream.writeObject(new ServerReturner(2, "false"));
                                    }

                                    break;
                                case 1:
                                    String session = ((TextMessage) command.getMessage()).getText();
                                    outputStream.writeObject(new ServerReturner(3, Main.dataManagement.getUser(Main.dataManagement.getId(session)).getUserName()));
                                    break;
                                case 2:
                                    outputStream.writeObject(new ServerReturner(4, ((TextMessage) command.getMessage()).getText() + "@" + Main.dataManagement.getPublicKey(((TextMessage) command.getMessage()).getText())));
                                    break;
                                case 3:
                                    int chatId = Main.dataManagement.createChat(id, command.getChatId(), ((TextMessage) command.getMessage()).getText().split(":")[1]);
                                    outputStream.writeObject(new ServerReturner(5, String.valueOf(chatId)));
                                    Main.dataManagement.addMessageT0(chatId, id, new TextMessage(((TextMessage) command.getMessage()).getText().split(":")[0]));
                                    break;
                                case 4:
                                    ClientMessage clientMessage = Main.dataManagement.getMessage(command.getChatId(), Integer.parseInt(((TextMessage) command.getMessage()).getText()));
                                    outputStream.writeObject(new ServerReturner(6, Encrypt.encrypt((new Gson()).toJson(clientMessage), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 5:
                                    String[] usernameMessage = ((TextMessage) command.getMessage()).getText().split(":");
                                    int userId = Main.dataManagement.getIdfromUsers(usernameMessage[0]);
                                    String message = usernameMessage[1];
                                    Main.dataManagement.addUsertoChat(userId, command.getChatId(), Main.dataManagement.addMessage(command.getChatId(), id, new TextMessage(message)));
                                    break;
                                case 6:
                                    String[] usernameRoles = ((TextMessage) command.getMessage()).getText().split(":");
                                    int userIdRole = Main.dataManagement.getIdfromUsers(usernameRoles[0]);
                                    String roles = usernameRoles[1];
                                    if (Main.dataManagement.doesUserHaveRole(command.getChatId(), id)) {
                                        Main.dataManagement.addRole(userIdRole, command.getChatId(), roles);
                                    } else {
                                        System.out.println("user isnt in the chat to add!");
                                    }
                                    break;
                                case 7:
                                    Main.dataManagement.createChannel(id, command.getChatId(), ((TextMessage) command.getMessage()).getText());
                                    break;
                                case 8:
                                    String[] messageReact = ((TextMessage) command.getMessage()).getText().split(":");
                                    int messageId = Integer.parseInt(messageReact[0]);
                                    int reactId = Integer.parseInt(messageReact[1]);
                                    Main.dataManagement.addReact(id, command.getChatId(), messageId, reactId);
                                    break;
                                case 9:
                                    Main.dataManagement.addPin(command.getChatId(), Integer.parseInt(((TextMessage) command.getMessage()).getText()), id);
                                    break;
                                case 10:
                                    Main.dataManagement.createPv(id, ((TextMessage) command.getMessage()).getText());
                                    break;
                                case 11:
                                    outputStream.writeObject(new ServerReturner(7, Encrypt.encrypt(Main.dataManagement.getPending2(id), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 12:
                                    Main.dataManagement.addFriends(id, command.getChatId());
                                    break;
                                case 13:
                                    Main.dataManagement.addMessage(command.getChatId(), id, (TextMessage) command.getMessage());
                                    for (Integer in: Server.connectedClients.keySet()) {
                                        System.out.println("tst46 "+ in);
                                        if(Main.dataManagement.isUserInChat(in,command.getChatId())){
                                            System.out.println("send notif to "+in+" for chat "+command.getChatId());
                                            Server.connectedClients.get(in).sendNotif(command.getChatId());
                                        }
                                    }
                                    break;
                                case 14:
                                    TextMessage newMessage = (TextMessage) command.getMessage();
                                    Main.dataManagement.addMessageT1(command.getChatId(), id, new TextMessage(newMessage.getText().split(":")[0]), Integer.parseInt(newMessage.getText().split(":")[1]));
                                    for (Integer in: Server.connectedClients.keySet()) {
                                        System.out.println("tst46 "+ in);
                                       if(Main.dataManagement.isUserInChat(in,command.getChatId())){
                                           System.out.println("send notif to "+in+" for chat "+command.getChatId());
                                           Server.connectedClients.get(in).sendNotif(command.getChatId());
                                       }
                                    }
                                    break;
                                case 15:
                                    System.out.println("tst41 "+Main.dataManagement.getSecCon(seConMessage.getChatId()));
                                    outputStream.writeObject(new ServerReturner(8, Encrypt.encrypt(Main.dataManagement.getPublicPrivateKey(id), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 16:
                                    outputStream.writeObject(new ServerReturner(9, Encrypt.encrypt(Main.dataManagement.getServers(id), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 17:
                                    outputStream.writeObject(new ServerReturner(10, Encrypt.encrypt(Main.dataManagement.getMessages(command.getChatId()), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 18:
                                    outputStream.writeObject(new ServerReturner(11, Encrypt.encrypt(Main.dataManagement.getChannels(command.getChatId()), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 19:
                                    outputStream.writeObject(new ServerReturner(12, Encrypt.encrypt(Main.dataManagement.getEncryption(id, command.getChatId()), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 20:
                                    outputStream.writeObject(new ServerReturner(13, Encrypt.encrypt(Main.dataManagement.getFriends(id), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 22:
                                    Main.dataManagement.removeUserfromChat(id, Main.dataManagement.getIdfromUsers(((TextMessage) command.getMessage()).getText()), command.getChatId());
                                    break;
                                case 21:
                                    Main.dataManagement.removeChannel(id, command.getChatId());
                                    break;
                                case 23:
                                    outputStream.writeObject(new ServerReturner(14, Encrypt.encrypt(Main.dataManagement.getChatUsers(command.getChatId()), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 24:
                                    if (Main.dataManagement.canUserCreatechatInChat(command.getChatId(), id)) {
                                        int chatinChatId = Main.dataManagement.createChat(id, command.getChatId(), ((TextMessage) command.getMessage()).getText().split(":")[1]);
                                        outputStream.writeObject(new ServerReturner(5, String.valueOf(chatinChatId)));
                                        Main.dataManagement.addMessageT0(chatinChatId, id, new TextMessage(((TextMessage) command.getMessage()).getText().split(":")[0]));
                                    }
                                    break;
                                case 25:
                                    Main.dataManagement.ChangeChannelName(id, command.getChatId(), ((TextMessage) command.getMessage()).getText());
                                    break;
                                case 26:
                                    outputStream.writeObject(new ServerReturner(10, Encrypt.encrypt(Main.dataManagement.getPins2(command.getChatId()), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 27:
                                    Main.dataManagement.changeStats(id, command.getChatId());
                                    break;
                                case 28:
                                    Main.dataManagement.changeUsername(id, ((TextMessage) command.getMessage()).getText());
                                    break;
                                case 29:
                                    Main.dataManagement.changeEmail(id, ((TextMessage) command.getMessage()).getText());
                                    break;
                                case 30:
                                    Main.dataManagement.changePhoneNumber(id, ((TextMessage) command.getMessage()).getText());
                                    break;
                                case 31:
                                    Main.dataManagement.changePassword(id, ((TextMessage) command.getMessage()).getText());
                                    break;
                                case 32:
                                    Main.dataManagement.rejectFriends(id, command.getChatId());
                                    break;
                                case 33:
                                    Main.dataManagement.blockChat(id, command.getChatId());
                                    break;
                                case 34:
                                    outputStream.writeObject(new ServerReturner(16, Encrypt.encrypt( Main.dataManagement.getBlocks(id), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 35:
                                    Main.dataManagement.unBlock(id,command.getChatId());
                                    break;
                                case 36:
                                    outputStream.writeObject(new ServerReturner(17, Encrypt.encrypt( Main.dataManagement.getSentPending(id), Main.dataManagement.getSecCon(seConMessage.getChatId()))));
                                    break;
                                case 37:
                                    Main.dataManagement.unsendFriendReq(id,command.getChatId());
                                    break;
                                case 38:
                                    new Thread(new ClientFile("C:\\Users\\NP\\IdeaProjects\\sepgor05\\src\\file\\"+((TextMessage) command.getMessage()).getText(),new Socket("localhost",10001))).start();
break;
                            }

                        }


                    } else {
                        System.out.println("message not supported!");
                    }
                }
                // client.close();
            } catch (IOException | ClassNotFoundException e) {
                try {
                    client.close();
                    disconnectedClient();
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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

    public void disconnectedClient() {
        Server.connectedClients.remove(userId);
        if (Main.dataManagement.getStats(userId) == 5) {
            Main.dataManagement.changeStats(userId, 4);
        }
    }
}
