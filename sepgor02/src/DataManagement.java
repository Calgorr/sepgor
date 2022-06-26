public interface DataManagement {

    User getUser(String userName);
    User getUser(int id);
    String getPassword(String userName);
    boolean IdIsValid (int id);
    int createChat(int id1,int id2);
    int addMessage(int chatId,int senderId,Message message);
    int getMessageNumber(int chatId);
    int getId(String session);
    ClientMessage getMessage(int chatId,int messageId);
    String getSecCon (int id);
    String insertSession(int id, String session);
    int getIdfromUsers(String userName);
    String getSesion(int id);
}
