package pkg;

import com.google.gson.Gson;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Main {
    public static DataManagement dataManagement = new SQL();

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {

        // write your code here
        // Connect.connect();
        //System.out.println(Functions.sha3_16("sepehrmnp"));
        //System.out.println(Functions.sha3("sepehrmnp"));
        //InsertApp.insertUser("calgor","calgor1381","ahomayoni3@gmail.com");
        //Connect.connect();
        //SQL.insertSession(SQL.getUser(1));
       // System.out.println(Login.login("sepehrmnp","sepehr138"));
       //Main.dataManagement.addPin(10,3,1);
//Main.dataManagement.addMessage(10,3,new TextMessage("salam man kheyliiii calgoram"));

        /*try {
            String encryptedString = Base64.getEncoder().encodeToString(RSAUtil.encrypt("Dhiraj is the author", RSAUtil.publicKey));
            System.out.println(encryptedString);
            String decryptedString = RSAUtil.decrypt(encryptedString, RSAUtil.privateKey);
            System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }*/
        //System.out.println(Secon.createSecon("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB"));

       // System.out.println(RSAUtil.decrypt("VG/mlSN6EMgj1wK6akg9cqXd7RjnWv4Ao/I2Vm7LhH0cSzyjRYLKRKKRnCfyA3AOSUhNAZCClnXEbQqfc9P0Er+B5Y+1mO5XmyFOX3QzFxITYgW9nkuvHwJAXVlo0aXBA2DS6BlIoQxGd5w6xi+nY1F2UjxK3Mq6lHJm5shtjA8=", "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm"));
        //System.out.println(Main.dataManagement.getPublicKey("calgor"));
       // dataManagement.createChat(1);
        //System.out.println(((new Gson()).fromJson(dataManagement.getMessage(13,2).message,TextMessage.class)).getText());
      //  Main.dataManagement.addReact(1,9,7,1);
       // System.out.println(Main.dataManagement.getPending(3));
        //Main.dataManagement.addFriends(3,13);
        System.out.println( Main.dataManagement.getChatUsers(15));
    }
}
