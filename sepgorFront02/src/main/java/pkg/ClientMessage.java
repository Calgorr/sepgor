package pkg;

public class ClientMessage {
    String senderId;
    int chatId;
    int messageId;
    int MessageType;
    String message;
    String like;
    String disliek;
    String laugh;
    String date;
    int replyId;

    public ClientMessage(String senderId, int chatId, int messageId, int messageType, String message, String like, String disliek, String laugh, String date, int replyId) {
        this.senderId = senderId;
        this.chatId = chatId;
        this.messageId = messageId;
        MessageType = messageType;
        this.message = message;
        this.like = like;
        this.disliek = disliek;
        this.laugh = laugh;
        this.date = date;
        this.replyId = replyId;
    }

    /*public String getMessageText(){

        }*/
    @Override
    public String toString() {
        return "ClientMessage{" +
                "senderId=" + senderId +
                ", chatId=" + chatId +
                ", messageId=" + messageId +
                ", MessageType=" + MessageType +
                ", message=" + message +
                ", date='" + date + '\'' +
                '}';
    }
}
