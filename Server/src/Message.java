import java.io.Serializable;

public class Message implements Serializable {
    private String session;
    private int command;
    private String chatId = "0000000000";
}
