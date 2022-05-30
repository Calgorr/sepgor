//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.util.HashSet;
//
//public class OutputThread implements Runnable {
//    private ObjectOutputStream outputStream;
//    private Socket client;
//    private HashSet<Socket> clients;
//    private Message message;
//
//    public OutputThread(Socket client, HashSet<Socket> clients, Message message) {
//        this.outputStream = outputStream;
//        this.client = client;
//        this.clients = clients;
//        this.message = message;
//    }
//
//
//    @Override
//    public void run() {
//        try {
//            outputStream = new ObjectOutputStream(client.getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (Socket other : clients) {
//            if (client == other) {
//                continue;
//            }
//            try {
//                outputStream.writeObject(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
