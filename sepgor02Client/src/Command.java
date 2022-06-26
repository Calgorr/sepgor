import com.google.gson.Gson;

import java.io.Serializable;

public class Command implements Serializable {
    private String session;
    private int command;
    private int chatId;
    private String message;
    private int type;

    public int getType() {
        return type;
    }

    public Message getMessage() {
        Message returner = null;
        if (type==1){
            returner = new Gson().fromJson(message,TextMessage.class);
        }
        return returner;

    }

    public Command(String session, int sender, int senderPort, Message message,int type) {
        this.session = session;
        this.command = sender;
        this.chatId = senderPort;
        this.message = new Gson().toJson(message);
        this.type = type;
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
