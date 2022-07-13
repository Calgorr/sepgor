package pkg;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ServerReturner implements Serializable {
    private int id ;
    private String text;

    public ServerReturner(int id, String text) {
        this.text = text;
        this.id = id;
        System.out.println(text);
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public String getServerKey() throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        System.out.println(text+"\n"+Main.content.privateKey);
        Main.content.serverKey =RSAUtil.decrypt(text,Main.content.privateKey);
        return Main.content.serverKey ;
    }
    public String getServerKey2() throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        //System.out.println(text+"\n"+Menu.tmpPrivateKey);
        Main.content.serverKey =RSAUtil.decrypt(text,Menu.tmpPrivateKey);
        return Main.content.serverKey ;
    }
}
