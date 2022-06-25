public class ClientMessage {
    Message message;
    int senderId;
    String date;

    public ClientMessage(Message message, int senderId, String date) {
        this.message = message;
        this.senderId = senderId;
        this.date = date;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "message=" + message.toString() +
                ", senderId=" + senderId +
                ", date='" + date + '\'' +
                '}';
    }
}
