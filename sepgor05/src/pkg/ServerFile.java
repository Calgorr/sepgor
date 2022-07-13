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
    ObjectInputStream in;
    Socket socket;

    public ServerFile(ObjectInputStream in, Socket socket) {
        this.in = in;
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            int type = ((Integer) in.readObject());
            String fileName = ((String) in.readObject());

            //Main.dataManagement.addMessageT2(Integer.parseInt(fileName[0]), Main.dataManagement.getIdfromUsers(fileName[1]), new TextMessage(fileName[3]));
            byte[] content = (byte[]) in.readObject();
            if(type==1) {
                String[] files = fileName.split(":");
                File file = new File("C:\\Users\\NP\\IdeaProjects\\sepgor05\\src\\file\\" + files[2]);
                Main.dataManagement.addMessageT2(Integer.parseInt(files[1]),Main.dataManagement.getId(files[0]),new TextMessage(files[2]));
                Files.write(file.toPath(), content);
            }else if(type == 2){
                File file = new File("C:\\Users\\NP\\IdeaProjects\\sepgor05\\src\\userProf\\" + Main.dataManagement.getId(fileName)+".jpeg");
                Files.write(file.toPath(), content);
            }else if(type == 3){
                File file = new File("C:\\Users\\NP\\IdeaProjects\\sepgor05\\src\\serverProf\\" + (fileName)+".jpeg");
                Files.write(file.toPath(), content);
            }
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