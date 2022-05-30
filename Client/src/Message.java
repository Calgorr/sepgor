import java.io.Serializable;

public class Message implements Serializable {
    private String message;
    private String sender;
    private int senderPort;

    public Message(String message, String sender, int senderPort) {
        this.message = message;
        this.sender = sender;
        this.senderPort = senderPort;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSenderPort() {
        return senderPort;
    }
}
