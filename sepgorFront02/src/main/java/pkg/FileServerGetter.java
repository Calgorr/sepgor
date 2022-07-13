package pkg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServerGetter implements Runnable {
    ServerSocket serverSocket;

    public FileServerGetter(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("salam");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread file = null;
            try {
                file = new Thread(new ServerFile(new ObjectInputStream(socket.getInputStream()),socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.start();
        }
    }
}
