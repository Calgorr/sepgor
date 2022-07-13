package pkg;

import com.example.sepgorfront01.HelloController;
import com.google.gson.Gson;
import org.w3c.dom.Text;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;


public class Main {
    public static com.example.sepgorfront01.HelloController controller = new com.example.sepgorfront01.HelloController();
    public static Content content;// new Content("", "", "","","sepehrmnp","sepehr1381");
    public static Client client;
    ///tmp data
    static String userNameToChat = "";//ino baadan id kon
    static String publicKeyToChat = "";


    public static void sendCommand(int command, int chatId, Message message) {
        String session = content.session;

        new OutputClient(client.socket, client.inputClient, new SeConMessage(content.serverId, Encrypt.encrypt(new Gson().toJson(new Command(session, command, chatId, message, 1)), content.serverKey))).start();
    }

    public static void askMessage(int chatId, int messageId) {
        sendCommand(4, chatId, new TextMessage(String.valueOf(messageId)));
    }

    public static void getPublicKey(String userName) {
        sendCommand(2, 0, new TextMessage(userName));
    }

    public static void createChat(String name) throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        //System.out.println(Base64.getEncoder().encodeToString(RSAUtil.encrypt(Functions.createSession(),content.publicKey)));
        sendCommand(3, 0, new TextMessage(Base64.getEncoder().encodeToString(RSAUtil.encrypt(Functions.createSession(), content.publicKey)) + ":" + name));
    }

    public static void createChatinChat(int chatId, String name) throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        //System.out.println(Base64.getEncoder().encodeToString(RSAUtil.encrypt(Functions.createSession(),content.publicKey)));
        sendCommand(3, chatId, new TextMessage(Base64.getEncoder().encodeToString(RSAUtil.encrypt(Functions.createSession(), content.publicKey)) + ":" + name));
    }

    public static void changeServerName(int ServerId, String serverName) {
        sendCommand(25, ServerId, new TextMessage(serverName));
    }
public static void getFile(String fileName){
        sendCommand(38,0,new TextMessage(fileName));
}
    public static void changeUsername(String username) {
        sendCommand(28, 0, new TextMessage(username));
    }

    public static void changeEmail(String username) {
        sendCommand(29, 0, new TextMessage(username));
    }

    public static void changePhoneNumber(String username) {
        sendCommand(30, 0, new TextMessage(username));
    }

    public static void changePassword(String username) {
        sendCommand(31, 0, new TextMessage(username));
    }

    public static void startChat() {
        if ((!userNameToChat.equals("")) && (!publicKeyToChat.equals(""))) {

        } else {
            System.out.println("need username And publickey");
        }
    }

    public static void addRole(String userName, int chatId, String roles) {

        sendCommand(6, chatId, new TextMessage(userName + ":" + roles));
    }

    public static void addChannel(int chatId, String name) throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        sendCommand(7, chatId, new TextMessage(name));

    }

    public static String fileSendName(String hashFile, String serverId) {
        System.out.println("your username:");
        String username = new Scanner(System.in).nextLine();
        return serverId + "-" + username + "-" + Encrypt.encrypt(hashFile, chatEncryption) + "-" + hashFile;
    }

    public static void removeChannel(int chatId) {
        sendCommand(21, chatId, new Message());
    }

    public static void removeFromChat(int chatId, String userId) {
        sendCommand(22, chatId, new TextMessage(userId));
    }

    public static void getPubPrv() throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {

        sendCommand(15, 0, new Message());

    }

    public static void getUsersOfChat(int chatId) {
        sendCommand(23, chatId, new Message());
    }

    public static String chatEncryption = "";
    public static boolean endShow = false;
    public static int chatId = 0;

    public static void showChat(int id) throws InterruptedException, IOException {
        chatId = id;
        sendCommand(19, id, new Message());
        sendCommand(17, id, new Message());
        boolean tr = true;
        while (true) {
            Thread.sleep(500);
            if (Main.endShow) {
                break;
            }
        }
        System.out.println("messages shown");
        /*
        while (tr) {
            System.out.println("""
                    1-Send Message
                    2-Send File
                    3-Pin message
                    4-React
                    5-Exit
                    """);

            switch (new Scanner(System.in).nextInt()) {
                case 1:
                    String message = new Scanner(System.in).nextLine();
                    int replyId = new Scanner(System.in).nextInt();
                    if (replyId == 0) {
                        sendMessage(id, message, chatEncryption);
                    } else {
                        sendMessage(id, message, chatEncryption, replyId);
                    }
                    break;
                case 2:
                    System.out.println("enter the absoloute path of the file");
                    String path = new Scanner(System.in).nextLine();
                    ServerFile server = new ServerFile();
                    server.serverFile();
                    ClientFile.Send(path);

                    break;
                case 3:
                    System.out.println("which Message id?");
                    addPin(id, new Scanner(System.in).nextInt());
                    break;
                case 4:
                    System.out.println("which Message id?");
                    int messageIdReact = new Scanner(System.in).nextInt();
                    System.out.println("which react?");
                    addReact(id, messageIdReact, new Scanner(System.in).nextInt());
                default:
                    System.out.println("ended messaging");
                    Main.chatEncryption = "";
                    endShow = false;
                    tr = false;
                    break;
            }
        }*/
    }
    public static void blockChat(int chatId){
        sendCommand(33,chatId,new Message());
    }
    public static void getFriends() {
        sendCommand(20, 0, new Message());
    }

    public static void showChannels(int id) {
        sendCommand(18, id, new Message());
    }

    public static void addToChat(int chatId, String userName, String encryption) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        userNameToChat = "";
        publicKeyToChat = "";
        getPublicKey(userName);
        while (true) {
            Thread.sleep(500);
            if (!userNameToChat.equals("")) {
                break;
            }
        }
        sendCommand(5, chatId, new TextMessage(userNameToChat + ":" + Base64.getEncoder().encodeToString(RSAUtil.encrypt(encryption, publicKeyToChat))));
    }

    public static void addReact(int chatId, int messageId, int reactId) {
        sendCommand(8, chatId, new TextMessage(messageId + ":" + reactId));
    }

    public static void addPin(int chatId, int messageId) {
        sendCommand(9, chatId, new TextMessage(String.valueOf(messageId)));
    }

    public static void createPv(String userName) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        userNameToChat = "";
        publicKeyToChat = "";
        getPublicKey(userName);
        while (true) {
            Thread.sleep(500);
            if (!userNameToChat.equals("")) {
                break;
            }
        }String encryption = Functions.createSession();

        sendCommand(10, 0, new TextMessage(userNameToChat + ":" + Base64.getEncoder().encodeToString(RSAUtil.encrypt(encryption, content.publicKey)) + ":" + Base64.getEncoder().encodeToString(RSAUtil.encrypt(encryption, publicKeyToChat))));
    }

    public static void getPending() {
        sendCommand(11, 0, new Message());
    }
    public static void getSentPending() {
        sendCommand(36, 0, new Message());
    }
    public static void unSendPending(int id) {
        sendCommand(37, id, new Message());
    }

    public static void getServers() {
        sendCommand(16, 0, new Message());
    }

    public static void addFriend(int id) {
        sendCommand(12, id, new Message());
    }
    public static void rejectFriend(int id) {
        sendCommand(32, id, new Message());
    }

    public static void sendMessage(int chatId, String text, String encryption) {
        sendCommand(13, chatId, new TextMessage(Encrypt.encrypt(text, encryption)));
    }

    public static void showPins(int chatId) {
        sendCommand(26, chatId, new Message());
    }

    public static void changeStats(int stats) {
        sendCommand(27, stats, new Message());
    }

    public static void sendMessage(int chatId, String text, String encryption, int reply) {
        sendCommand(14, chatId, new TextMessage(Encrypt.encrypt(text, encryption) + ":" + String.valueOf(reply)));
    }
    public static void getBlocks(){
        sendCommand(34,0,new Message());
    }
    public static void unBlock(int id){
        sendCommand(35,id,new Message());
    }
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException, InterruptedException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        ///load publickey and private
        if (saveContents.load()) {
            System.out.println("loaded keys successfully");
        } else {
            content = new Content("", "", "", "", "sepehrmnp", "sepehr1381");
            new RSAKeyPairGenerator();
            System.out.println("didnt have privateKey so i generated");

        }
        System.out.println("sepehr " + content.privateKey + "  " + content.publicKey + " serverKey: " + content.serverKey);

        ///load serverKey

        client = new Client();
        client.connect();
        Socket socket = client.socket;
        content.validKey2(client.socket, client.inputClient);
        content.validSession(client.socket, client.inputClient, "sepehrmnp", "sepehr1381");

        //sendCommand(4,0,new TextMessage("calgor"));
        //getPublicKey("calgor");
        // sendCommand(0,0,new TextMessage("fff"));
        // createChat();
        //addToChat(9,"calgor","d8eea58c2822dcdd");
        // addRole("calgor",9,"11111111");
        //System.out.println(RSAUtil.decrypt("HIYL358mHF8bX+v5iq3hUm4jnlz7CJWfcYjht41MMEqFGBmXQbJcCcX0whCayIuYGQK7s1N+02oQTNB1+AihCGKkK+1dxrcG/Jd+Ch+zPZygAslEt4IgXLWeP902FP64A/J+UKRi3NQNxaBuoaRdZgYl9fHPIXxG2HqQbLwU9Po=", "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAK67gcCMElsaUgdsSW1ubj3UW5tb9qe54OM2G8gIRhdWDFZRB16kZRE1zGGiMEL4UeMDn6vtO15NTOfSeAqjHru32eteOgHxe8f3Xt+pyVYWeXoK2EwM19ENpoPlUL0ETMRejriqONowQYxU13Xv4KFpDrqRiHtHay9GX16BNxGhAgMBAAECgYA/69qIvPVOHK/bhVLLVLXBqrkkeZ0fONG7bST2pYg2q0TyIsxmZA+RHzQ4l2O+bCdQvBeihrPMSM0UlZtSkOABwx28pi+3jeFTwhVPiWbmJ+S37f5IHagqpl1Q+ErEdk+iKh8YI5v6GIPXiwQMdnJ6hf6hZdrU9PwGi8GQFsYyHQJBAOLTK1r2xFxZ26zVtijHublrv4vXV4/vin0a+FkUSsCGdyHMLdpwgQ7KB1EDtS9DMYhGYoQjFgXG/0EcCNuxxLcCQQDFNQ0r2FOdeR8n3/y+5pnIHEjCZlFwULDCn9GTsSg+1ygn73t1zLU18pHfXesd1+iud5rhIloJOFjP4jNK4nRnAkB87dHzR/be5p8Xkp3B2D9y4OWfH/waStT8Et62MXb6kxUKiGq45T4v5xYgiOpCHcAqTpn0LV7zz6bHNamrVz4BAkA2DvXeV/2LxEELIDZbakErfr1fNMcMP8JEh6TelIiQuVATqm0wfH4MHMNp9IfX9XCeKsE1B8KglmaaDcq7s1nvAkBQGV369Npq6MIlo2rIN7W769Ilxy5iDNugqECzsSbWOYXrETsIl3eYs9W1Q+3YH+bfQi3vOsYr5XsrCEyd5zh3"));
//askMessage(8,1);
        //addChannel(9);
        //createPv("calgor");
        // getPending();
//addFriend(13);
        // sendMessage(13,"fuck you calgor very much","e71df9e8eb0a7458",3);
        saveContents.save();
    }
}
