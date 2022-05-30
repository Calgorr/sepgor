public class Client {
    public static void main(String[] args) {
        Message message = new Message(0, "null", "null");
        while (true) {
            Thread clientThread = new Thread(new OutputThread(message));
            clientThread.start();
        }

    }
}
