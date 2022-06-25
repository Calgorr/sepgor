import java.io.Serializable;

public class SeConMessage implements Serializable {
    private String text;
    private int id;

    public SeConMessage(int id, String text) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public int getChatId() {
        return id;
    }
}
