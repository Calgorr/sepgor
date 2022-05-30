//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.Socket;
//import java.util.HashSet;
//
//public class InputThread implements Runnable {
//    private HashSet<Socket> clients;
//    private ObjectInputStream inputStream;
//    private OutputThread outputThread;
//    private Socket client;
//
//    public InputThread(Socket client, HashSet<Socket> clients) {
//        this.client = client;
//        this.clients = clients;
//    }
//
//    @Override
//    public void run() {
//        try {
//            inputStream = new ObjectInputStream(client.getInputStream());
//            Message message;
//            while (client.isConnected() && (message = (Message) inputStream.readObject()) != null) {
//                if (message.getMessage().equals("#exit")) {
//                    break;
//                }
//                outputThread = new OutputThread(client, clients, message);
//                Thread thread = new Thread(outputThread);
//                thread.start();
//                thread.interrupt();
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//}
