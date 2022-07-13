package pkg;

public class Filemanagement implements DataManagement{


    @Override
    public User getUser(String userName) {
        return null;
    }

    @Override
    public User getUser(int id) {
        return null;
    }

    @Override
    public String getPassword(String userName) {
        return null;
    }

    @Override
    public boolean IdIsValid(int id) {
        return false;
    }

    @Override
    public int createChat(int id1, int superserver,String name) {
        return 0;
    }





    @Override
    public int addMessage(int chatId, int senderId, Message message) {
        return 0;
    }

    @Override
    public int addMessageT0(int chatId, int senderId, Message message) {
        return 0;
    }

    @Override
    public int getMessageNumber(int chatId) {
        return 0;
    }

    @Override
    public int getId(String session) {
        return 0;
    }

    @Override
    public ClientMessage getMessage(int chatId, int messageId) {
        return null;
    }


    @Override
    public String getSecCon(int id) {
        return null;
    }

    @Override
    public String insertSession(int id, String session) {
        return null;
    }

    @Override
    public int getIdfromUsers(String userName) {
        return 0;
    }

    @Override
    public String getSesion(int id) {
        return null;
    }

    @Override
    public String getPublicKey(String userName) {
        return null;
    }

    @Override
    public void addUsertoChat(int id, int chatId, int chatAddedMessage) {

    }

    @Override
    public boolean isUserInChat(int id, int chatId) {
        return false;
    }

    @Override
    public void addRole(int id, int chatId, String roles) {

    }

    @Override
    public boolean doesUserHaveRole(int chatId, int id) {
        return false;
    }

    @Override
    public int createChannel(int id1, int superserver,String name) {
        return 0;
    }

    @Override
    public void addReact(int user, int chatId, int messageId, int reactId) {

    }

    @Override
    public void addPin(int chatId, int messageId, int userId) {

    }

    @Override
    public int createPv(int id1, String encrypted) {
        return 0;
    }

    @Override
    public String getPending2(int id) {
        return null;
    }

    @Override
    public String getPending(int id) {
        return null;
    }

    @Override
    public void removeChannel(int id, int chatId) {

    }

    @Override
    public void addFriends(int id, int chatId) {

    }

    @Override
    public int addMessageT1(int chatId, int senderId, Message message, int reply) {
        return 0;
    }

    @Override
    public int addMessageT2(int chatId, int senderId, Message message) {
        return 0;
    }

    @Override
    public int insertUser(String userName, String passWord, String email, String publicKey, String privateKey) {
        return 0;
    }

    @Override
    public String getPublicPrivateKey(int id) {
        return null;
    }

    @Override
    public String getServers(int userId) {
        return null;
    }

    @Override
    public String getMessages(int chatId) {
        return null;
    }

    @Override
    public String getChannels(int channelId) {
        return null;
    }

    @Override
    public String getEncryption(int userId, int chatId) {
        return null;
    }

    @Override
    public String getFriends(int id) {
        return null;
    }

    @Override
    public void removeUserfromChat(int removerId,int id, int chatId) {

    }

    @Override
    public String getChatUsers(int id) {
        return null;
    }

    @Override
    public boolean canUserCreatechatInChat(int chatId, int user) {
        return false;
    }

    @Override
    public void ChangeChannelName(int id,int chatId, String newName) {

    }

    @Override
    public String showPins(int chatId) {
        return null;
    }

    @Override
    public void changeStats(int id, int stats) {

    }

    @Override
    public int getStats(int userId) {
        return 0;
    }

    @Override
    public void changeUsername(int id, String newUserName) {

    }

    @Override
    public void changeEmail(int id, String newEmail) {

    }

    @Override
    public void changePhoneNumber(int id, String newPhoneNumber) {

    }

    @Override
    public void changePassword(int id, String newPassword) {

    }

    @Override
    public String getPins2(int chatId) {
        return null;
    }

    @Override
    public void rejectFriends(int id, int chatId) {

    }

    @Override
    public void blockChat(int id, int chatId) {

    }

    @Override
    public String getBlocks(int id) {
        return null;
    }

    @Override
    public void unBlock(int id, int chatId) {

    }

    @Override
    public String getSentPending(int id) {
        return null;
    }

    @Override
    public void unsendFriendReq(int id, int chatId) {

    }


}
