import java.io.Serializable;

public class TextMessage extends Message implements Serializable {
    private String text;

    public TextMessage(String text) {

        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
