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
    public int createChat(int id1, int id2) {
        return 0;
    }

    @Override
    public int addMessage(int chatId, int senderId, Message message) {
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


}
