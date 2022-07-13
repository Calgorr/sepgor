package pkg;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
    private static final HashSet<UserHandler> clients = new HashSet<>();


    static DataManagement dataManagement = new SQL();
    public static ServerSocket serverSocketFile;

    public static HashMap<Integer,UserHandler> connectedClients = new HashMap<>();

public static Socket fileSocketSender;
    static {

        try {
            serverSocketFile = new ServerSocket(1212);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //dataManagement.createChat(1,3);
        //dataManagement.addMessage(1,3,new TextMessage("hi bitch"));
        //System.out.println(dataManagement.getMessageNumber(1));
        //System.out.println(dataManagement.getId("c6edf82283a44921"));
        //System.out.println(dataManagement.getMessage(1,2));
        try {
            ServerSocket serverSocket = new ServerSocket(6009);
             new Thread(new FileServerGetter(serverSocketFile)).start();
             new Thread(new StackServer()).start();
            while (true) {
                Socket client;
                client = serverSocket.accept();
                UserHandler userHandler = new UserHandler(client);
                clients.add(userHandler);
                userHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static HashSet<UserHandler> getClients() {
        return clients;
    }

}
