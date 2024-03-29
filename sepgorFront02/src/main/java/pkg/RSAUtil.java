package pkg;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {
    /**
     * some utilities for RSA
     * @throws NoSuchAlgorithmException
     */
    /*static RSAKeyPairGenerator keyPair;

    static {
        try {
            keyPair = new RSAKeyPairGenerator();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }*/

  //  static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB"; //= Base64.getEncoder().encodeToString(keyPair.getPublicKey().getEncoded());//
   // static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";//=Base64.getEncoder().encodeToString(keyPair.getPrivateKey().getEncoded()); //
    //static String serverKey ="s";
    public RSAUtil() throws NoSuchAlgorithmException {
    }

    public static PublicKey getPublicKey(String base64PublicKey) {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException {

        System.out.println(RSAUtil.decrypt("Tt3raHqrf6viqh2weF1SagqwzkVD95qeR1PL/C96N6yCmG4iZJoX8+4Ps99HqybP6yb8zQwzS9afQgkq0tcK3SkIqpBi7QKU3FchylxPFX3XXshe85LvoZxqh+tWUigM0DHmNOIhlDGmIdA2C/qnqy9VF+I2cI2WEfz9zeofOzw=","MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIKyQ2UefeDb/S9qhSA9gn+yykWbFzj8emO3m5ghmMvlVJBZp1BfleXRyB7taBqgUrXguBxYDPBlvhErcMVJfObpWjEogXmHoYVi9UxyEq/iQLehmNeqWag5lJSgEhuHWy2Hg/aa4eXG85/fxbnSsu5yBjug6TtMXwgk+BWhUwLrAgMBAAECgYAisZmMXTTDHQKvtJIURd+/cEGbipj2iqcek13dW4XJEl6JVxNAFa+f6zk5ZIIPF2H9X+Lfu0vsAMYcnrB33pMl/+Fzf0Smnm79b8HubX1uE6TtbgEyr4BNTk3Sy8Isz1v2RdjZsGxaGKRhMxj66jrg1EJ5BPiRwK3Qw0aNFOS/qQJBAInNqnJtyUQZVKVPdRI2hfxCSgiA9AsDMHdWqejGwdLimelNn2SWp+1wUgsdZSUzM2BIqlTyF6G2yjduocAKUw0CQQDyzA+RCV/2OWR7rJuJTNCTgCBq+z5FuPWYFLpD2qr7183RwfrXQz5dYiNqHfLzfMWphezkZ+9skMPC1/Pwj4/XAkEAgiF1lBmYtnY1OOaP52MVo+H1h5I+4ydf3VI5oVgQMNVMAsUKgDLE4bPvxg88NKTlH0d/kjgLmOrEixQuKuuVAQJBANyoWildFMpoxIqlh0GT/ydeVZgEuEaTXxiVFedfDvAM4/ATVE5YvZAPBdKGX2usZNO5dF7smqrXMiKdMwTY5EcCQEMwL9H1JW4a1klsw751Rd50nwfwYdpy+dkeX2pzah3r1Mlc07MNViv8rPSpLMMo57tXJUPqljbi1BS9vOKh04A="));
       /* try {
            String encryptedString = Base64.getEncoder().encodeToString(encrypt("Dhiraj is the author", Main.content.publicKey));
            System.out.println(encryptedString);
            System.out.println(Main.content.privateKey);
            String decryptedString = RSAUtil.decrypt(encryptedString, Main.content.privateKey);
            System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }*/

    }
}
