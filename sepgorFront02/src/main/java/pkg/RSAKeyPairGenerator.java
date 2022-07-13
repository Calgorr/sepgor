package pkg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class RSAKeyPairGenerator {
    /**
     * Generates RSA key
     */

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();

        ///newcmnd
        //Main.content.privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        //Main.content.publicKey= Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());

        //System.out.println("sep 02" + Main.content.privateKey+"   "+Main.content.publicKey);
        //System.out.println("sep 02" + Main.content.privateKey+"   "+Main.content.publicKey);
        //Main.content = new Content(pub,prv,"","","sepehrmnp","sepehr1381");
        Menu.tmpPrivateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        Menu.tmpPublicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
    }

    public static void KeyPairGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

        keyGen =KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
    KeyPair pair = keyGen.generateKeyPair();

    ///newcmnd
    Main.content.privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
    Main.content.publicKey= Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
}
    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
       // keyPairGenerator.writeToFile("RSA/publicKey", keyPairGenerator.getPublicKey().getEncoded());
        //keyPairGenerator.writeToFile("RSA/privateKey", keyPairGenerator.getPrivateKey().getEncoded());
        //System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
       // System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
    }
}
