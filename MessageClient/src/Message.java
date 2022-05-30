import java.io.Serializable;

public class Message implements Serializable {
    private int chatId;
    private String sender;
    private String text;

    public Message(int chatId, String sender, String text) {
        this.chatId = chatId;
        this.sender = sender;
        this.text = text;
    }
}
