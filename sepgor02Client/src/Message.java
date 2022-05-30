import java.io.Serializable;

public class Message implements Serializable {
    private String session;
    private int command;
    private String chatId;
    private String text;

    public Message(String message, int sender, String senderPort,String text) {
        this.session = message;
        this.command = sender;
        this.chatId = senderPort;
        this.text = text;
    }

    public String getSession() {
        return session;
    }

    public int getCommand() {
        return command;
    }

    public String getChatId() {
        return chatId;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
