package pkg;

import pkg.Main;
import pkg.TextMessage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

/**
 * this class is used to transfer a file between server and a client
 */
public class ServerFile implements Runnable {
    /**
     * every file transfer between server and client is handled is here
     */
    ObjectInputStream in;
    Socket socket;

    public ServerFile(ObjectInputStream in, Socket socket) {
        this.in = in;
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            String fileName = ((String) in.readObject());
            //Main.dataManagement.addMessageT2(Integer.parseInt(fileName[0]), Main.dataManagement.getIdfromUsers(fileName[1]), new TextMessage(fileName[3]));
            byte[] content = (byte[]) in.readObject();

            File file = new File("C:\\Users\\NP\\IdeaProjects\\sepgorFront02\\src\\main\\rsrs\\" + fileName);
                Files.write(file.toPath(), content);

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}