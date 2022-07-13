package pkg;

import java.io.*;

public abstract class saveContents implements Serializable {

    static void save() throws IOException {//saves notebook obj with all the notes at the end
        FileOutputStream fileOutputStream = new FileOutputStream("data.txt" , false);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(Main.content);
        objectOutputStream.close();
        fileOutputStream.close();
    }



   static boolean load() throws IOException, ClassNotFoundException {//loads notebook obj on start
        try {
            FileInputStream fileInputStream = new FileInputStream("data.txt");

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Main.content = (Content) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
