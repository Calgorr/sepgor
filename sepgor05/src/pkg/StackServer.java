package pkg;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class StackServer implements Runnable{
    public static HashMap<Integer, ArrayList<Socket>> voiceChat= new HashMap<>();

    public StackServer() {
    }

    @Override
    public void run() {

        try {
            ServerSocket server=new ServerSocket(10002);

        while (true) {
            Socket client = server.accept();

            Thread thread = new Thread(new ServerThread(client));
            thread.start();
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
