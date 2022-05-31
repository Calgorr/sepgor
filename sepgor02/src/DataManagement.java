public interface DataManagement {

    User getUser(String userName);
    User getUser(int id);
    String getPassword(String userName);
    boolean IdIsValid (int id);
    int insertSession(User user, String session);
}
