package pkg;

import pkg.Encrypt;
import pkg.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Scanner;

/**
 * this class is used to transfer a file between server and a client
 */
public class ClientFile implements Runnable {
    String path;
    MessageDigest shaDigest;
    Socket socket;

    public ClientFile(String path,Socket socket) {
        this.path = path;
this.socket = socket;
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        ;

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }



    @Override
    public void run() {
        try {
            File file = new File(path);
            //String hash = getFileChecksum(shaDigest, file);
            // Get the size of the file
            byte[] content = Files.readAllBytes(file.toPath());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            //ino dorost kon
            out.writeObject(file.getName());
          out.writeObject(content);
            out.close();
            socket.close();
        } catch (IOException e) {

        }
    }
}