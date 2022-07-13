package pkg;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu0 {
    static Scanner input = new Scanner(System.in);

    public static void menu() {
        System.out.println("""
                1-sign in
                2-sign up""");
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
            System.out.println("Wrong input!");
        }
    }

    private static void signUp(String username, String password, String email) throws NoSuchAlgorithmException {
        RSAKeyPairGenerator.KeyPairGenerator();
        OutputClient outputClient = new OutputClient(Main.client.socket, Main.client.inputClient, new SeConMessage(-2,Main.content.serverId+":"+Encrypt.encrypt(email+":"+username+":"+Functions.sha3(password)+":"+Main.content.publicKey+":"+Encrypt.encrypt(Main.content.privateKey,password),Main.content.serverKey)));
        outputClient.start();
    }
    static String tmpUsername="";
    static String tmpPassword="";
    private static void signIn(String username, String password) throws NoSuchAlgorithmException, InterruptedException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException, IOException {
        tmpUsername=username;
        tmpPassword = password;
        Main.content.session="";
        Main.content.privateKey="";
        Main.content.publicKey="";

        Main.content.validSession(Main.client.socket, Main.client.inputClient, username, password);
        while (true){
            Thread.sleep(500);
            if(!Main.content.session.equals("")){
                break;
            }
        }


        Main.getPubPrv();
        while (true){
            Thread.sleep(500);
            if(!Main.content.privateKey.equals("")){
                break;
            }
        }
        System.out.println("session: "+Main.content.session+"   public key: "+Main.content.publicKey+"   private key: "+Main.content.privateKey);
        System.out.println("""
                1-Friends list
                2-Pending requests
                3-Show a server
                4-Create a server
                5-Block list
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
            signInBlockList();
        } else if (order1 == 6) {
            menu();
        }
    }

    private static void friendsList() throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        System.out.println("""
                1-Add Friend""");
        int order1 = input.nextInt();
        if (order1 == 1) {
            System.out.println("Username");

            String username = new Scanner(System.in).nextLine();
            Main.createPv(username);
        } else {

        }
    }

    private static void pendingRequests() {
        Main.getPending();
        System.out.println("which one to add?");

    }

    private static void signInShowServer() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InterruptedException, IOException {
        Main.getServers();
        System.out.println("which server to open?");
        int serverId =new Scanner(System.in).nextInt();
        System.out.println("""
                1-Show chats
                2-Show channels
                3-Voice Chat
                4-Add Role
                5-Add Channel
                """);
        switch (input.nextInt()){
            case 1:
                Main.showChat(serverId);
                break;
            case 2:
                Main.showChannels(serverId);
                Main.showChat(new Scanner(System.in).nextInt());
                break;
            case 5:
               // Main.addChannel(serverId);
                break;

        }

    }

    private static void signInCreateServer() {

    }

    private static void signInBlockList() {

    }

    static String tmpPublicKey = "";
    static String tmpPrivateKey = "";

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
        Main.client = new Client();
        Main.client.connect();
        new RSAKeyPairGenerator();
        Socket socket = Main.client.socket;
        Main.content = new Content("", "", "", "", "", "");
        Main.content.validKey2(Main.client.socket, Main.client.inputClient);
        while (Main.content.serverKey==null){}
        menu();
    }
}