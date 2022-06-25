import java.security.NoSuchAlgorithmException;

public class  Login {
    static DataManagement dataManagement = Main.dataManagement;
    public static int login(String userName,String password) throws NoSuchAlgorithmException {
       int error = 0;
        String passwordDB = dataManagement.getPassword(userName);
       if(passwordDB!=null){
           if(passwordDB.equals(Functions.sha3(password))){
               String session = Functions.createSession();
               dataManagement.insertSession(dataManagement.getUser(userName),session);
               System.out.println(session);
           }else error = 2;
       }else{
           error =1;
       }
       return error;
    }
    //public static int getKey
}
