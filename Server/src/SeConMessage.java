import java.io.Serializable;

public class SeConMessage implements Serializable {
    private int chatId;
    private String text;

    public SeConMessage(int chatId, String text) {
        this.chatId = chatId;
        this.text = text;
    }
}