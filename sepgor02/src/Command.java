import java.io.Serializable;

public class Command implements Serializable {
    private String session;
    private int command;
    private int chatId;
    private Message message;

    public Message getMessage() {
        return message;
    }

    public Command(String session, int sender, int senderPort, Message message) {
        this.session = session;
        this.command = sender;
        this.chatId = senderPort;
        this.message = message;
    }

    public String getSession() {
        return session;
    }

    public int getCommand() {
        return command;
    }

    public int getChatId() {
        return chatId;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
}
