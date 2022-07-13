package pkg;

public interface DataManagement {

    User getUser(String userName);
    User getUser(int id);
    String getPassword(String userName);
    boolean IdIsValid (int id);
    int createChat(int id1,int superserver,String name);
    int addMessage(int chatId,int senderId,Message message);
    int addMessageT0(int chatId,int senderId,Message message);
    int getMessageNumber(int chatId);
    int getId(String session);
    ClientMessage getMessage(int chatId,int messageId);
    String getSecCon (int id);
    String insertSession(int id, String session);
    int getIdfromUsers(String userName);
    String getSesion(int id);
    String getPublicKey (String userName);
    void addUsertoChat(int id,int chatId,int chatAddedMessage);
    boolean isUserInChat(int id,int chatId);
    void addRole(int id,int chatId,String roles);
    boolean doesUserHaveRole(int chatId,int id);
    int createChannel(int id1,int superserver,String name);
    void addReact(int user,int chatId,int messageId,int reactId);
    void addPin(int chatId,int messageId,int userId);
    int createPv(int id1,String encrypted);
    String getPending2(int id);
    String getPending(int id);
    void removeChannel(int id ,int chatId);
    void addFriends(int id,int chatId);
    int addMessageT1(int chatId,int senderId,Message message,int reply);

    int addMessageT2(int chatId,int senderId,Message message);
    int insertUser(String userName, String passWord,String email,String publicKey,String privateKey);
    String getPublicPrivateKey (int id);
    String getServers(int userId);
    public String getMessages(int chatId);
    String getChannels(int channelId);
    String getEncryption(int userId,int chatId);
    String getFriends(int id);
    void removeUserfromChat(int removerId,int id,int chatId);
    String getChatUsers(int id);
    boolean canUserCreatechatInChat(int chatId,int user);
    void ChangeChannelName(int id,int chatId,String newName);
    String showPins(int chatId);
    void changeStats(int id,int stats);
    int getStats(int userId);
    void changeUsername(int id,String newUserName);
    void changeEmail(int id,String newEmail);
    void changePhoneNumber(int id,String newPhoneNumber);
    void changePassword(int id,String newPassword);
    String getPins2(int chatId);
    void rejectFriends(int id,int chatId);
    void blockChat(int id,int chatId);
    String getBlocks(int id);
    void unBlock(int id,int chatId);
    String getSentPending(int id);
    void unsendFriendReq(int id,int chatId);


}
