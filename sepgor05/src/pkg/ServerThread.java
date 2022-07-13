package pkg;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread implements Runnable {
    Socket client;
    byte[] data = new byte[4096];
    Integer chatID;

    public ServerThread(Socket client) throws LineUnavailableException {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            chatID = (Integer) new ObjectInputStream(client.getInputStream()).readObject();
            if (StackServer.voiceChat.containsKey(chatID)) {
                StackServer.voiceChat.get(chatID).add(client);
            } else {
                StackServer.voiceChat.put(chatID, new ArrayList<>());
                StackServer.voiceChat.get(chatID).add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                client.getInputStream().read(data);
                broadcastToAllClients(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void broadcastToAllClients(byte[] data) throws IOException {
        for (int i = 0; i < StackServer.voiceChat.get(chatID).size(); i++) {
            if (this.client != StackServer.voiceChat.get(chatID).get(i)) {
               try{
                   client.getOutputStream().write(data);
               }catch (Exception e){
                   StackServer.voiceChat.get(chatID).remove(client);
               }
            }
        }
        /*for  clients : StackServer.voiceChat.) {
            if (client == this.client)
                continue;
            client.getOutputStream().write(data);
        }*/

    }
}
