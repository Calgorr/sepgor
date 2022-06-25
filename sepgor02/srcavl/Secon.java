import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class Secon {
    public static String createSecon(String publicKey) throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        String passcon=Functions.createSession();
        int id = SQL.insertSecon(passcon);
        System.out.println(id +","+passcon);
        return Base64.getEncoder().encodeToString(RSAUtil.encrypt(id +","+passcon, publicKey));
    }
}