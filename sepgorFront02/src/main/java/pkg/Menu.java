package pkg;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {
    private static int voice = 0;
    //private static StackServer server = null;
    static Scanner input = new Scanner(System.in);

    public static void menu() {
        System.out.println("""
                1-sign in
                2-sign up
                3-exit""");
        int order1 = input.nextInt();
        input.nextLine();
        try {
            if (order1 == 1) {
                System.out.println("""
                        Username : """);
                String username = input.nextLine();
                System.out.println("""
                        Password""");
                String password = input.nextLine();
                signIn(username, password);

            } else if (order1 == 2) {
                String email;
                do {
                    System.out.println("""
                            Email :""");
                    email = input.nextLine();
                }
                while (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email));
                String username;
                do {
                    System.out.println("""
                            Username :""");
                    username = input.nextLine();
                }
                while (!Pattern.matches("^[a-zA-z]{6,}\\d*$", username));
                String password;
                do {
                    System.out.println("""
                            Password :""");
                    password = input.nextLine();
                }
                while (Pattern.matches("^[a-zA-z]{8,}\\d*$", password));
                System.out.println("inputs were correct");
                signUp(username, password, email);
            }
        } catch (Exception e) {

        }
    }

    public static void signUp(String username, String password, String email) throws NoSuchAlgorithmException {
        RSAKeyPairGenerator.KeyPairGenerator();
        OutputClient outputClient = new OutputClient(Main.client.socket, Main.client.inputClient, new SeConMessage(-2, Main.content.serverId + ":" + Encrypt.encrypt(email + ":" + username + ":" + Functions.sha3(password) + ":" + Main.content.publicKey + ":" + Encrypt.encrypt(Main.content.privateKey, password), Main.content.serverKey)));
        outputClient.start();
    }

    static String tmpUsername = "";
    static String tmpPassword = "";

    public static void signIn(String username, String password) throws NoSuchAlgorithmException, InterruptedException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException, LineUnavailableException, IOException {
        //Main.content.session = "";
        //Main.content.privateKey = "";
       // Main.content.publicKey = "";

        //Main.content.validSession(Main.client.socket, Main.client.inputClient, username, password);
      while (true) {
            Thread.sleep(500);
            if (!Main.content.session.equals("")) {
                break;
            }
        }
      ///del
        System.out.println("session is "+Main.content.session);
     Main.getPubPrv();
      /*  while (true) {
            Thread.sleep(1000);
            if (!Main.content.privateKey.equals("")) {
                break;
            }
        }*/

        System.out.println("session: " + Main.content.session + "   public key: " + Main.content.publicKey + "   private key: " + Main.content.privateKey);
       // signInMenu();
    }

    private static void signInMenu() throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException, LineUnavailableException, IOException {
        System.out.println("""
                1-Friends list
                2-Pending requests
                3-Show a server
                4-Create a server
                5-profile
                6-log Out""");
        int order1 = input.nextInt();
        if (order1 == 1) {
            friendsList();
        } else if (order1 == 2) {
            pendingRequests();
        } else if (order1 == 3) {
            signInShowServer();
        } else if (order1 == 4) {
            signInCreateServer();
        } else if (order1 == 5) {
            prof();
        } else if (order1 == 6) {
            menu();
        }
    }
    private static void prof() throws LineUnavailableException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, InterruptedException {
        System.out.println("""
                1-Upload photo
                2-change Status
                """);
        switch (new Scanner(System.in).nextInt()){
            case 1:
                break;
            default:
                System.out.println("which stat?");
                Main.changeStats(new Scanner(System.in).nextInt());
                break;

        }signInMenu();
    }

    private static void friendsList() throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException, LineUnavailableException, IOException {
        Main.getFriends();
        System.out.println("""
                1-Add new Friend
                2-Return""");
        int order1 = input.nextInt();
        if (order1 == 1) {
            System.out.println("Username");
            String username = new Scanner(System.in).nextLine();
            Main.createPv(username);
        } else {
            signInMenu();
        }
    }

    private static void pendingRequests() throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException, LineUnavailableException, IOException {
        Main.getPending();
        System.out.println("""
                1-Add to friends
                2-Return""");
        switch (input.nextInt()) {
            case 1:
                Main.addFriend(new Scanner(System.in).nextInt());
                break;
            case 2:
                signInMenu();
        }

    }

    private static void signInShowServer() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InterruptedException, LineUnavailableException, IOException {
        Main.getServers();
        System.out.println("which server to open?");
        int serverId = new Scanner(System.in).nextInt();
        Main.sendCommand(19, serverId, new Message());
        System.out.println("""
                1-Show chats
                2-Show channels
                3-Voice Chat
                4-Add Role
                5-Add Channel
                6-Add member
                7-removeChannel
                8-removeUser
                9-Show users
                10-createPRVChat
                11-change server name
                12-Show pins
                13-Return""");
        switch (input.nextInt()) {
            case 1:
                Main.showChat(serverId);
                signInShowServerShowChat();
                break;
            case 2:
                Main.showChannels(serverId);
                signInShowServerShowChannels();
                break;
            case 3:
                startVoiceChat(voice++);
                break;
            case 4:
                System.out.println("username? ");
                String usernme = new Scanner(System.in).nextLine();
                System.out.println("roles? ");
                String roles = new Scanner(System.in).nextLine();
                Main.addRole(usernme, serverId, roles);
                signInMenu();
                break;
            case 5:
                System.out.println("channel name: ");
                Main.addChannel(serverId, new Scanner(System.in).nextLine());
                signInMenu();
                break;
            case 6:

                System.out.println("username:");
                Main.addToChat(serverId, new Scanner(System.in).nextLine(), Main.chatEncryption);
                signInMenu();
                break;
            case 7:
                Main.showChannels(serverId);
                System.out.println("which one to delete?");
                Main.removeChannel(new Scanner(System.in).nextInt());
                signInMenu();
                break;
            case 8:
                System.out.println("username?");
                Main.removeFromChat(serverId, new Scanner(System.in).nextLine());
                signInMenu();
                break;
            case 9:
                Main.getUsersOfChat(serverId);
                Thread.sleep(1500);

                signInShowServer();
                break;
            case 10:
                System.out.println("chat Name?");
                Main.createChatinChat(serverId, new Scanner(System.in).nextLine());
                signInMenu();
                break;
            case 11:
                System.out.println("new serverName?");
                Main.changeServerName(serverId, new Scanner(System.in).nextLine());
                signInMenu();
                break;
            case 12:
                Main.showPins(serverId);
                signInMenu();
                break;
            default:
                signInMenu();
                break;

        }

    }

    private static void signInCreateServer() throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException, LineUnavailableException, IOException {
        System.out.println("sever name: ");
        Main.createChat(new Scanner(System.in).nextLine());
        signInMenu();
    }

    private static void signInBlockList() {

    }

    static String tmpPublicKey = "";
    static String tmpPrivateKey = "";

    private static void signInShowServerShowChat() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InterruptedException, LineUnavailableException, IOException {
        System.out.println("Enter the chatID or enter -1 to Return back");
        int order = new Scanner(System.in).nextInt();

        if (order == -1)
            signInShowServer();
        else {
            Main.showChat(order);
        }
    }

    private static void signInShowServerShowChannels() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InterruptedException, LineUnavailableException, IOException {
        System.out.println("Enter the channelID or enter -1 to Return back");
        int order = new Scanner(System.in).nextInt();
        if (order == -1)
            signInShowServer();
        else {
            Main.showChat(order);
            signInMenu();
        }
    }

    private static void startVoiceChat(int voice) throws IOException, LineUnavailableException {
    /*    if (voice == 0) {
            server = new StackServer();
        }
        server.connect();
        StackClient client = new StackClient();
        client.connect();
    */}

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
        Main.client = new Client();
        Main.client.connect();
        new RSAKeyPairGenerator();
        Socket socket = Main.client.socket;
        Main.content = new Content("", "", "", "", "", "");
        Main.content.validKey2(Main.client.socket, Main.client.inputClient);
        while (Main.content.serverKey == null) {
        }
        menu();
    }
}