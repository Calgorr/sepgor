package pkg;

import com.google.gson.Gson;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQL implements DataManagement {
    /**
     * the class that is used for the whole database management
     * @return
     */
    //static Connection conn = null;
    private static Connection connect() {
        // SQLite connection string
        Connection conn = null;
        if(conn!=null) return conn;
        //String url = "jdbc:sqlite:C:\\Users\\NP\\IdeaProjects\\sepgor02\\sepgor02.db";
        String url = "jdbc:sqlite:sepgor02.db";

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public int insertUser(String userName, String passWord,String email,String publicKey,String privateKey) {
        String sql = "INSERT INTO USERS(userName,password,email,status,chatIds,date) VALUES(?,?,?,?,?,?)";
        int error = 0,id=0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, passWord);
            pstmt.setString(3, email);
            pstmt.setInt(4, 4);
            pstmt.setString(5, "");
            pstmt.setString(6, Functions.getTimeStamp());
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id = Integer.parseInt(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            error = e.getErrorCode();
        }

        String sql2 = "INSERT INTO 'pubs'(id,publicKey,privateKey) VALUES(?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, publicKey);
            pstmt.setString(3, privateKey);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            error = e.getErrorCode();
        }
        return error;
    }
    public String getPassword(String userName){
        String sql = "SELECT password FROM USERS WHERE userName = '"+userName+"'";
        String password = null;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            //System.out.println(rs.getString("password")+);
            if(rs.next()) password = rs.getString("password");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return password;
    }
    public String insertSession(int id, String session) {///tghr

        String sql = "INSERT INTO sessions(id,session,date) VALUES(?,?,?)";
        boolean tr = true;
        int error =0;
        while(tr) {
            tr = false;
            idCheckAndRemoveSession(id);
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2,session);
                pstmt.setString(3,Functions.getTimeStamp());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                if(e.getErrorCode()==19){
                    tr = true;
                }
                error = e.getErrorCode();
            }
        }
        return session;
    }
    public int getMessageNumber(int chatId){
        int messageNum = 0;
        String sql = "SELECT messagesNumber FROM chatIds WHERE chatId= "+chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) messageNum = rs.getInt("messagesNumber");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messageNum;
    }
    public void removeUserfromChat(int removerId,int id,int chatId){
        if(canUserRemoveUser(chatId,removerId) && !doesUserHaveRole(chatId,id)){

        String sql = "SELECT users FROM chatIds WHERE chatId= "+chatId;
        String[] users = new String[0];
        String newUsers = "";
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) users = rs.getString("users").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < users.length; i++) {
                if(!users[i].equals(String.valueOf(id))){
                    newUsers+=users[i]+",";

                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query = "UPDATE chatIds SET users=? WHERE chatId="+chatId;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,newUsers.substring(0,newUsers.length()-1));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }


        String sql2 = "SELECT chatIds FROM USERS WHERE id= "+id;
        String[] servers = new String[0];
        String newServers = "";
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql2);
            if(rs.next()) servers = rs.getString("chatIds").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < servers.length; i++) {
                if(!servers[i].split(":")[0].equals(String.valueOf(chatId))){
                    newServers+=servers[i]+",";
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String query2 = "UPDATE USERS SET chatIds=? WHERE id="+id;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query2);
            statement.setString(1,newServers.substring(0,newServers.length()-1));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }}

    }
    public boolean isUserInChat(int id,int chatId){

        String sql2 = "SELECT type,subserver FROM chatIds WHERE chatId= "+chatId;
        int type = 0;
        String subservers="";
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql2);
            if(rs.next()){
                type = rs.getInt("type");
                subservers = rs.getString("subserver");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(type==1){
            chatId = Integer.parseInt(subservers.split(",")[0]);
        }

        boolean isInChat = false;
        String sql = "SELECT users FROM chatIds WHERE chatId= "+chatId;
        String[] users = new String[0];
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) users = rs.getString("users").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < users.length; i++) {
                if(users[i].equals(String.valueOf(id))){
                    isInChat =true;
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isInChat;
    }
    public boolean doesUserHaveRole(int chatId,int id){
        boolean isInChat = false;
        String sql = "SELECT role FROM chatIds WHERE chatId= "+chatId;
        String[] users = new String[0];
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) users = rs.getString("role").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < users.length; i++) {
                if(users[i].split(":")[0].equals(String.valueOf(id))){
                    isInChat =true;
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isInChat;
    }
    public String getRoles(int chatId){

            String sql = "SELECT role FROM chatIds WHERE chatId = "+chatId;
            String returner = "";
            try {
                Connection conn = connect();

                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql);
                if(rs.next()){
                    returner +=rs.getString("role");
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            return returner+",";
        }

public void addRole(int id,int chatId,String roles){

    String usersRoles =getRoles(chatId);
    if(!doesUserHaveRole(chatId,id)){
        String query = "UPDATE chatIds SET role=? WHERE chatId="+chatId;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,usersRoles+id+":"+roles);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }}
    }


    public int addMessage(int chatId,int senderId,Message message){
        int error =0;
        int messageId = getMessageNumber(chatId)+1;
        int messageType=1;
        ///check type
        if(message instanceof TextMessage) messageType =1;

        String sql = "INSERT INTO 'messages' (chatId,messageId,senderId,messageType,message,like,dislike,laugh,date) VALUES(?,?,?,?,?,?,?,?,?)";
        if(isUserInChat(senderId,chatId)){
            try (Connection conn = connect();
                      PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, chatId);
                pstmt.setInt(2, messageId);
                pstmt.setInt(3, senderId);
                pstmt.setInt(4, messageType);
                pstmt.setString(5,(new Gson()).toJson(message));
                pstmt.setString(6,"");
                pstmt.setString(7,"");
                pstmt.setString(8,"");
                pstmt.setString(9,Functions.getTimeStamp());
                pstmt.execute();

                } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else{
            error = 2;
            ///del
            System.out.println("user wants to fuck!");
        }

        String query = "UPDATE chatIds SET messagesNumber=? WHERE chatId="+chatId;

        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, messageId);
            statement.executeUpdate();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }

        return messageId;
    }
    public int addMessageT2(int chatId,int senderId,Message message){
        int error =0;
        int messageId = getMessageNumber(chatId)+1;
        int messageType=3;
        ///check type


        String sql = "INSERT INTO 'messages' (chatId,messageId,senderId,messageType,message,like,dislike,laugh,date) VALUES(?,?,?,?,?,?,?,?,?)";
        if(isUserInChat(senderId,chatId)){
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, chatId);
                pstmt.setInt(2, messageId);
                pstmt.setInt(3, senderId);
                pstmt.setInt(4, messageType);
                pstmt.setString(5,(new Gson()).toJson(message));
                pstmt.setString(6,"");
                pstmt.setString(7,"");
                pstmt.setString(8,"");
                pstmt.setString(9,Functions.getTimeStamp());
                pstmt.execute();
                System.out.println("tst46");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else{
            error = 2;
            ///del
            System.out.println("user wants to fuck!");
        }

        String query = "UPDATE chatIds SET messagesNumber=? WHERE chatId="+chatId;

        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, messageId);
            statement.executeUpdate();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }

        return messageId;
    }


    public int addMessageT0(int chatId,int senderId,Message message){
        int error =0;
        int messageId = getMessageNumber(chatId)+1;
        int messageType=0;
        ///check type


        String sql = "INSERT INTO 'messages' (chatId,messageId,senderId,messageType,message,like,dislike,laugh,date) VALUES(?,?,?,?,?,?,?,?,?)";
        if(isUserInChat(senderId,chatId)){
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, chatId);
                pstmt.setInt(2, messageId);
                pstmt.setInt(3, senderId);
                pstmt.setInt(4, messageType);
                pstmt.setString(5,(new Gson()).toJson(message));
                pstmt.setString(6,"");
                pstmt.setString(7,"");
                pstmt.setString(8,"");
                pstmt.setString(9,Functions.getTimeStamp());
                pstmt.execute();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else{
            error = 2;
            ///del
            System.out.println("user wants to fuck!");
        }

        String query = "UPDATE chatIds SET messagesNumber=? WHERE chatId="+chatId;

        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, messageId);
            statement.executeUpdate();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }

        return error;
    }

    public int addMessageT1(int chatId,int senderId,Message message,int reply){
        int error =0;
        int messageId = getMessageNumber(chatId)+1;
        int messageType=2;
        ///check type


        String sql = "INSERT INTO 'messages' (chatId,messageId,senderId,messageType,message,like,dislike,laugh,replyId,date) VALUES(?,?,?,?,?,?,?,?,?,?)";
        if(isUserInChat(senderId,chatId)){
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, chatId);
                pstmt.setInt(2, messageId);
                pstmt.setInt(3, senderId);
                pstmt.setInt(4, messageType);
                pstmt.setString(5,(new Gson()).toJson(message));
                pstmt.setString(6,"");
                pstmt.setString(7,"");
                pstmt.setString(8,"");
                pstmt.setInt(9,reply);
                pstmt.setString(10,Functions.getTimeStamp());
                pstmt.execute();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else{
            error = 2;
            ///del
            System.out.println("user wants to fuck!");
        }


        String query = "UPDATE chatIds SET messagesNumber=? WHERE chatId="+chatId;

        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, messageId);
            statement.executeUpdate();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }

        return error;
    }





        public boolean idExistSession(int id){
        String sql = "SELECT * FROM sessions WHERE id ="+id+" LIMIT 1";
        boolean exist = false;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) exist = true;
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exist;
    }
    public void idCheckAndRemoveSession(int id){
        String sql = "DELETE FROM sessions WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                //pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public int getId(String session){
        int id=0;
        String sql = "SELECT id FROM sessions WHERE session ='"+session+"' LIMIT 1";
       // boolean exist = false;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) id = rs.getInt("id");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    public String getSesion(int id){//jdid
        String session = null;
        String sql = "SELECT session FROM sessions WHERE id ="+id+" LIMIT 1";
        // boolean exist = false;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) session = rs.getString("session");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return session;
    }

    public int getIdfromUsers(String userName){
        int id =0;
        String sql = "SELECT id FROM USERS WHERE userName ='"+userName+"' LIMIT 1";
        // boolean exist = false;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) id = rs.getInt("id");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    public String getUserNameFromId(int id){
        String sql = "SELECT * FROM USERS WHERE id ="+id+" LIMIT 1";
        String userName = "";
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()){
                userName = rs.getString("userName");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userName;
    }

    public User getUser(int id){
        String sql = "SELECT * FROM USERS WHERE id ="+id+" LIMIT 1";
        User user = null;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()){
                user = new User(id,rs.getString("userName"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public int sessionAdd(){
      int returner = 0;

      return returner;
    }
    public String getChatsofUser(int id){
        String sql = "SELECT chatIds FROM USERS WHERE id = "+id;
        String returner = "";
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()){
                returner +=rs.getString("chatIds");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return (!returner.equals("null"))?returner+",":"";
    }
    public int addChatToUser(int id, int chatId,int chatAddedMessage){
        int error =0;
        String chats =getChatsofUser(id);
        String query = "UPDATE USERS SET chatIds=? WHERE id="+id;

        try (Connection conn = connect();){
             PreparedStatement statement = conn.prepareStatement(query);
             statement.setString(1, chats+chatId+":"+chatAddedMessage);
             statement.executeUpdate();
        }
     catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            error = e.getErrorCode();
        }
        return error;
    }
    public String getUsersFromChat(int chatId){
        String users = "";
        String sql = "SELECT users FROM chatIds WHERE chatId= "+chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) users = rs.getString("users");
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users.length()>0?users+",":"";
    }
    public String getSubServersFromChat(int chatId){
        String users = "";
        String sql = "SELECT subserver FROM chatIds WHERE chatId= "+chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) users = rs.getString("subserver");
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users.length()>0?users+",":"";
    }
    public void addUsertoChat(int id,int chatId,int chatAddedMessage){
        addChatToUser(id,chatId,chatAddedMessage);
        String users =getUsersFromChat(chatId);
        String query = "UPDATE chatIds SET users=? WHERE chatId="+chatId;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, users+id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }
    }
    public int createPv(int id1,String encrypted) {
        String sql = "INSERT INTO ChatIds(users,messagesNumber,type,subserver,role,pins) VALUES(?,?,?,?,?,?)";
        String[] encryptedIdand1and2 = encrypted.split(":");
        int id2 = getIdfromUsers(encryptedIdand1and2[0]);
        int id = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "" + id1+","+id2);
            pstmt.setInt(2, 0);
            pstmt.setInt(3, -1);
            pstmt.setString(4, "");
            pstmt.setString(5, ""+id1+":11111111"+","+id2+":11111111");
            pstmt.setString(6, "");
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id = Integer.parseInt(rs.getString(1));
            }
            addChatToUser(id1, id,1);
            addChatToUser(id2, id,2);

            addMessageT0(id,id1,new TextMessage(encryptedIdand1and2[1]));
            addMessageT0(id,id2,new TextMessage(encryptedIdand1and2[2]));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());

        }
        return id;
    }


    public int createChat(int id1,int superserver,String name) {
        String sql = "INSERT INTO ChatIds(users,messagesNumber,type,subserver,role,pins,name) VALUES(?,?,?,?,?,?,?)";

        int id = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "" + id1);
            pstmt.setInt(2, 0);
            pstmt.setInt(3, 0);
            pstmt.setString(4, ""+superserver);
            pstmt.setString(5, ""+id1+":11111111");
            pstmt.setString(6, "");
            pstmt.setString(7, name);
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id = Integer.parseInt(rs.getString(1));
            }
            addChatToUser(id1, id,1);
            ///del
            System.out.println("tst01");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());

        }
        return id;
    }
public void removeChannel(int id ,int chatId){
        ///nc
    ///remove from user too!

    String sql2 = "SELECT type,subserver FROM chatIds WHERE chatId= "+chatId;
    int type = 0;
    String subservers="";
    try {
        Connection conn = connect();
        Statement stmt  = conn.createStatement();
        ResultSet rs    = stmt.executeQuery(sql2);
        if(rs.next()){
            type = rs.getInt("type");
            subservers = rs.getString("subserver");
        }
        rs.close();
        stmt.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    if(type==1){
        chatId = Integer.parseInt(subservers.split(",")[0]);
    }


    if(canUserCanRemoveChannel(chatId,id)){
    String sql = "DELETE FROM chatIds WHERE chatId = ?";

    try (Connection conn = this.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // set the corresponding param
        pstmt.setInt(1, chatId);
        // execute the delete statement
        pstmt.executeUpdate();

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    }
}
    public int createChannel(int id1,int superserver,String name) {
        int id = 0;
        if(canUserCreateChannel(superserver,id1)) {
           String sql = "INSERT INTO ChatIds(messagesNumber,type,subserver,pins,name) VALUES(?,?,?,?,?)";
           ///if statement to check if user has the permission to add channel

           try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
               pstmt.setInt(1, 0);
               pstmt.setInt(2, 1);
               pstmt.setString(3, "" + superserver);
               pstmt.setString(4, "");
               pstmt.setString(5, name);
               pstmt.execute();
               ResultSet rs = pstmt.getGeneratedKeys();
               if (rs.next()) {
                   id = Integer.parseInt(rs.getString(1));
               }

           } catch (SQLException e) {
               System.out.println(e.getMessage());
               System.out.println(e.getErrorCode());

           }
           String subservers = getSubServersFromChat(superserver);

           String query = "UPDATE chatIds SET subserver=? WHERE chatId=" + superserver;
           try (Connection conn = connect();) {
               PreparedStatement statement = conn.prepareStatement(query);
               statement.setString(1, subservers + id);
               statement.executeUpdate();
           } catch (SQLException e) {
               System.out.println(e.getMessage());
               System.out.println(e.getErrorCode());
           }
       }
        return id;
    }

    public boolean IdIsValid (int id){
        String sql = "SELECT passcon FROM secon WHERE id = "+id;
        User user = null;
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()){
                rs.close();
                stmt.close();
                return true;
            }else{
                rs.close();
                stmt.close();

                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String getSecCon (int id){
        String sql = "SELECT passcon FROM secon WHERE id = "+id;
        String seCon = "";
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            while (rs.next()){
                seCon = rs.getString("passcon");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return seCon;
    }

    public User getUser(String userName){
        String sql = "SELECT id FROM USERS WHERE userName = '"+userName+"'";
        User user = null;
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()){
                user = new User(rs.getInt("id"),userName);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }
    public static int insertSecon(String passcon){
        String sql = "INSERT INTO secon(passcon,date) VALUES(?,?)";
        int id = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, passcon);
            pstmt.setString(2, Functions.getTimeStamp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            //error = e.getErrorCode();
        }

        sql = "SELECT id FROM secon WHERE passcon = '"+passcon+"' LIMIT 1";
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()){
                id = rs.getInt("id");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;

    }

    public ClientMessage getMessage(int chatId,int messageId){
        String sql = "SELECT message,senderId,chatId,messageId,messageType,like,dislike,laugh,date,replyId FROM 'messages' WHERE chatId = "+chatId+" AND messageId ="+messageId;
        ClientMessage message=null;
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()){

                if(rs.getInt("messageType")==1 || rs.getInt("messageType")==0||rs.getInt("messageType")==2) {
                    message = new ClientMessage(getUserNameFromId(rs.getInt("senderId")),rs.getInt("chatId"),rs.getInt("messageId"),rs.getInt("messageType"),rs.getString("message"),rs.getString("like"),rs.getString("dislike"),rs.getString("laugh"), rs.getString("date"),rs.getInt("replyId"));
                }

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }


    public String getMessage2(int chatId,int messageId){
        String sql = "SELECT message,senderId,chatId,messageId,messageType,like,dislike,laugh,date,replyId FROM 'messages' WHERE chatId = "+chatId+" AND messageId ="+messageId;
        String message=null;
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()){

                if(rs.getInt("messageType")==1 || rs.getInt("messageType")==0||rs.getInt("messageType")==2) {
                    message = new Gson().toJson(new ClientMessage(getUserNameFromId(rs.getInt("senderId")),rs.getInt("chatId"),rs.getInt("messageId"),rs.getInt("messageType"),rs.getString("message"),rs.getString("like"),rs.getString("dislike"),rs.getString("laugh"), rs.getString("date"),rs.getInt("replyId")));
                }

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }


    ////jdid
    public String getPublicKey (String userName){
        int id = getIdfromUsers(userName);
        String sql = "SELECT publicKey FROM 'pubs' WHERE id = "+id;
        String publicKey = "";
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            while (rs.next()){
                publicKey = rs.getString("publicKey");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return publicKey;
    }
    public String getPublicPrivateKey (int id){
        String sql = "SELECT publicKey,privateKey FROM 'pubs' WHERE id = "+id;
        String publicKey = "";
        String privateKey = "";
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            while (rs.next()){
                publicKey = rs.getString("publicKey");
                privateKey = rs.getString("privateKey");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return publicKey+":"+privateKey;
    }

    public String getReact(int chatId,int messageId,int reactId){
        String react = "";
        if(reactId==1){
        String sql = "SELECT like FROM 'messages' WHERE chatId="+chatId+" AND messageId ="+messageId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            while (rs.next()){
                react = rs.getString("like");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }else if(reactId==2){
            String sql = "SELECT dislike FROM 'messages' WHERE chatId="+chatId+" AND messageId ="+messageId;
            try {
                Connection conn = connect();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql);
                while (rs.next()){
                    react = rs.getString("dislike");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }else if(reactId==3){
            String sql = "SELECT laugh FROM 'messages' WHERE chatId="+chatId+" AND messageId ="+messageId;
            try {
                Connection conn = connect();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql);
                while (rs.next()){
                    react = rs.getString("laugh");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
        return react.equals("")?"":react+",";
    }
    public boolean didReact(int user,int chatId,int messageId){
        String laughs = getReact(chatId,messageId,3);
        String dislikes = getReact(chatId,messageId,2);
        String likes = getReact(chatId,messageId,1);
        String reacts = "";
        if(!(laughs+dislikes+likes).equals("")) {
            if (!laughs.equals("")) reacts += laughs + ",";
            if (!dislikes.equals("")) reacts += dislikes + ",";
            if (!likes.equals("")) reacts += likes + ",";
            reacts=reacts.substring(0,reacts.length()-1);
        }
        String[] reactsList =reacts.split(",");
        //String[] reacts = (laughs+","+dislikes+","+likes).split(",");
        boolean isInReacts = false;
        if(!reactsList[0].equals("")){
        for (int i = 0; i < reactsList.length; i++) {
            if(Integer.parseInt(reactsList[i])==user){
                isInReacts = true;
                break;
            }
        }
        }
        return isInReacts;
    }
    public void addReact(int user,int chatId,int messageId,int reactId){
        if(isUserInChat(user,chatId)&&!didReact(user,chatId,messageId)){
        String reacts = getReact(chatId,messageId,reactId);
        if(reactId==1){
            String query = "UPDATE 'messages' SET like=? WHERE chatId="+chatId+" AND messageId ="+messageId;
            try (Connection conn = connect();){
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, reacts+user);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getErrorCode());
            }
        }else if (reactId==2){
            String query = "UPDATE 'messages' SET dislike=? WHERE chatId="+chatId+" AND messageId ="+messageId;
            try (Connection conn = connect();){
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, reacts+user);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getErrorCode());
            }
        }else if(reactId==3){
            String query = "UPDATE 'messages' SET laugh=? WHERE chatId="+chatId+" AND messageId ="+messageId;
            try (Connection conn = connect();){
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, reacts+user);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getErrorCode());
            }
        }
        }else {
            System.out.println("user is not in chat");
        }
    }
    public boolean isMessageInChat(int chatId,int messageId){
        boolean isInChat = false;

        String sql = "SELECT messageType FROM 'messages' WHERE chatId= "+chatId+" AND messageId= "+messageId;

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) {

                isInChat = true;
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isInChat;
    }
    public boolean canUserPin(int chatId,int user){
        boolean canPin = false;

        String sql = "SELECT role FROM chatIds WHERE chatId= "+chatId;
        String[] roles = new String[0];
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) roles = rs.getString("role").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < roles.length; i++) {
                if(roles[i].split(":")[0].equals(String.valueOf(user))){
                    if(roles[i].split(":")[1].charAt(7)=='1'){
                        canPin =true;
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return canPin;
    } public boolean canUserCreateChannel(int chatId,int user){
        boolean canPin = false;

        String sql = "SELECT role FROM chatIds WHERE chatId= "+chatId;
        String[] roles = new String[0];
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) roles = rs.getString("role").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < roles.length; i++) {
                if(roles[i].split(":")[0].equals(String.valueOf(user))){
                    if(roles[i].split(":")[1].charAt(0)=='1'){
                        canPin =true;
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return canPin;
    }public boolean canUserCreatechatInChat(int chatId,int user){
        boolean canPin = false;

        String sql = "SELECT role FROM chatIds WHERE chatId= "+chatId;
        String[] roles = new String[0];
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) roles = rs.getString("role").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < roles.length; i++) {
                if(roles[i].split(":")[0].equals(String.valueOf(user))){
                    if(roles[i].split(":")[1].charAt(3)=='1'){
                        canPin =true;
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return canPin;
    }
    public boolean canUserRemoveUser(int chatId,int user){
        boolean canPin = false;

        String sql = "SELECT role FROM chatIds WHERE chatId= "+chatId;
        String[] roles = new String[0];
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) roles = rs.getString("role").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < roles.length; i++) {
                if(roles[i].split(":")[0].equals(String.valueOf(user))){
                    if(roles[i].split(":")[1].charAt(2)=='1'){
                        canPin =true;
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return canPin;
    }

    public boolean canUserCanRemoveChannel(int chatId,int user){
        boolean canPin = false;

        String sql = "SELECT role FROM chatIds WHERE chatId= "+chatId;
        String[] roles = new String[0];
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) roles = rs.getString("role").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < roles.length; i++) {
                if(roles[i].split(":")[0].equals(String.valueOf(user))){
                    if(roles[i].split(":")[1].charAt(1)=='1'){
                        canPin =true;
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return canPin;
    } public boolean canUserchanegServerName(int chatId,int user){
        boolean canPin = false;

        String sql = "SELECT role FROM chatIds WHERE chatId= "+chatId;
        String[] roles = new String[0];
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) roles = rs.getString("role").split(",");
            rs.close();
            stmt.close();
            for (int i = 0; i < roles.length; i++) {
                if(roles[i].split(":")[0].equals(String.valueOf(user))){
                    if(roles[i].split(":")[1].charAt(5)=='1'){
                        canPin =true;
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return canPin;
    }
    public String getPins(int chatId){
        String pins = "";
        String sql = "SELECT pins FROM chatIds WHERE chatId= "+chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) pins = rs.getString("pins");
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return pins.length()>0?pins+",":"";
    }
    public String getPins2(int chatId){
        String pins = "";
        String sql = "SELECT pins FROM chatIds WHERE chatId= "+chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) pins = rs.getString("pins");
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("tst23"+pins);
        String[] pinsId = pins.split(",");
        String pinMessages="";
        for (int i = 0; i < pinsId.length; i++) {
            System.out.println("tst22"+pinsId[i]);
            pinMessages+=getMessage2(chatId,Integer.parseInt(pinsId[i]))+";";
        }
        System.out.println("tst21"+pinMessages);
        return pinMessages.length()>0?pinMessages.substring(0,pinMessages.length()-1):"";
    }
    public void addPin(int chatId,int messageId,int userId){
        String sql2 = "SELECT type,subserver FROM chatIds WHERE chatId= "+chatId;
        int type = 0;
        String subservers="";
        int chatId0=chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql2);
            if(rs.next()){
                type = rs.getInt("type");
                subservers = rs.getString("subserver");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(type!=0){
            chatId0 = Integer.parseInt(subservers.split(",")[0]);
        }

        String pins = getPins(chatId);
        if(canUserPin(chatId0,userId) && isMessageInChat(chatId,messageId)){

            String query = "UPDATE chatIds SET pins=? WHERE chatId="+chatId;
            try (Connection conn = connect();){
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, pins+messageId);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getErrorCode());
            }
        }
    }


    public int getType(int chatId,int user){
        int type = 0;
        String sql =  "SELECT type,users FROM chatIds WHERE chatId= "+chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) {
                if(!(rs.getInt("type")==0)){
                if(Integer.parseInt((rs.getString("users")).split(",")[1])==user) {
                    type = rs.getInt("type");
                }}
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return type;
    }
    public String getPending(int id){
        String[] pendingList = new String[0];
        String sql = "SELECT chatIds FROM USERS WHERE id= "+id;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) pendingList = rs.getString("chatIds").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String pendingReturn =  "";
        ///del
        for (int i = 0; i < pendingList.length; i++) {
            System.out.println("tst27 "+pendingList[i]);
        }


        for (int i = 0; i < pendingList.length; i++) {
            if(getType(Integer.parseInt(pendingList[i].split(":")[0]),id)==-1){
                pendingReturn+=pendingList[i].split(":")[0]+",";
            }
        }
        return pendingReturn.length()>0?pendingReturn.substring(0,pendingReturn.length()-1):"";

    }

    public String getSentPending(int id){
        String[] pendingList = new String[0];
        String sql = "SELECT chatIds FROM USERS WHERE id= "+id;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) pendingList = rs.getString("chatIds").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String pendingReturn =  "";



        for (int i = 0; i < pendingList.length; i++) {
            if(getType2(Integer.parseInt(pendingList[i].split(":")[0]))==-1&&getPVCreator(Integer.parseInt(pendingList[i].split(":")[0]))==id){
                pendingReturn+=pendingList[i].split(":")[0]+":"+getUserNameFromId(getFriendFromChat(Integer.parseInt(pendingList[i].split(":")[0]),id))+",";
            }
        }
        return pendingReturn.length()>0?pendingReturn.substring(0,pendingReturn.length()-1):"";

    }

    public int getPVCreator(int chatId){
        int creator = 0;
        String sql =  "SELECT type,users FROM chatIds WHERE chatId= "+chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) {
                if(!(rs.getInt("type")==0)) {
                    creator = Integer.parseInt(rs.getString("users").split(",")[0]);
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return creator;
    }
    public String getPending2(int id){
        String[] pendingList = new String[0];
        String sql = "SELECT chatIds FROM USERS WHERE id= "+id;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) pendingList = rs.getString("chatIds").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String pendingReturn =  "";
        for (int i = 0; i < pendingList.length; i++) {
            if(getType(Integer.parseInt(pendingList[i].split(":")[0]),id)==-1){
                pendingReturn+=pendingList[i].split(":")[0]+":"+getUserNameFromId(getPVCreator(Integer.parseInt(pendingList[i].split(":")[0])))+",";
            }
        }
       pendingReturn =  pendingReturn.length()>0?pendingReturn.substring(0,pendingReturn.length()-1):"";
        pendingReturn+="&"+getSentPending(id);
        ///del
        System.out.println("tst29 "+pendingReturn);
        return pendingReturn.equals("&")?"":pendingReturn;

    }

    public void addFriends(int id,int chatId){
        String[] pending = getPending(id).split(",");
        boolean isInPending =false;
        for (int i = 0; i < pending.length; i++) {
            if(pending[i].equals(String.valueOf(chatId))){
                isInPending = true;
                break;
            }
        }
        if(isInPending){

            String query = "UPDATE chatIds SET type=? WHERE chatId="+chatId;
            try (Connection conn = connect();){
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, 2);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getErrorCode());
            }

        }


    }
    public void rejectFriends(int id,int chatId){
        String[] pending = getPending(id).split(",");
        boolean isInPending =false;
        for (int i = 0; i < pending.length; i++) {
            if(pending[i].equals(String.valueOf(chatId))){
                isInPending = true;
                break;
            }
        }
        if(isInPending){

            String query = "UPDATE chatIds SET type=? WHERE chatId="+chatId;
            try (Connection conn = connect();){
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, -2);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getErrorCode());
            }

        }


    }
    public String getChannels(int channelId){
       ArrayList<String> channels = new ArrayList<String>();
        String sql = "SELECT chatId FROM chatIds WHERE subserver= '"+channelId+"'";
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            while(rs.next()) {
                channels.add(rs.getString("chatId"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String channelsName = "";
        for (int i = 0; i < channels.size(); i++) {
            channelsName+= channels.get(i)+":"+getServerName(Integer.parseInt(channels.get(i)))+",";
        }
        return channelsName.length()>0?channelsName.substring(0,channelsName.length()-1):"";

    }
    public String getServerName(int id){
        String serverName = "";
        String sql = "SELECT name FROM chatIds WHERE chatId= "+id;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) serverName = rs.getString("name");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return serverName;
    }
    public String getServers2(int userId){
        String[] servers = new String[0];
        String sql = "SELECT chatIds FROM USERS WHERE id= "+userId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) servers = rs.getString("chatIds").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String serversNames = "";
        if(!servers[0].equals("")){
            for (int i = 0; i < servers.length; i++) {
                if(getType2(Integer.parseInt(servers[i].split(":")[0]))==-3 ||getType2(Integer.parseInt(servers[i].split(":")[0]))==-4){
                    serversNames += Integer.parseInt(servers[i].split(":")[0])+":"+getServerName(Integer.parseInt(servers[i].split(":")[0]))+",";
                }
            }}
        return serversNames.length()>0?serversNames.substring(0,serversNames.length()-1):"";

    }
    public String getServers(int userId){
        String[] servers = new String[0];
        String sql = "SELECT chatIds FROM USERS WHERE id= "+userId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) servers = rs.getString("chatIds").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String serversNames = "";
        if(!servers[0].equals("")){
        for (int i = 0; i < servers.length; i++) {
            if(getType2(Integer.parseInt(servers[i].split(":")[0]))==0){
            serversNames += Integer.parseInt(servers[i].split(":")[0])+":"+getServerName(Integer.parseInt(servers[i].split(":")[0]))+",";
        }
        }}
        return serversNames.length()>0?serversNames.substring(0,serversNames.length()-1):"";

    }public String getMessages(int chatId){
        String sql = "SELECT message,senderId,chatId,messageId,messageType,like,dislike,laugh,date,replyId FROM 'messages' WHERE chatId = "+chatId;
        String messages="";
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            while (rs.next()){

                if(rs.getInt("messageType")==1 ||rs.getInt("messageType")==2||rs.getInt("messageType")==3) {
                    messages += new Gson().toJson(new ClientMessage(getUserNameFromId(rs.getInt("senderId")),rs.getInt("chatId"),rs.getInt("messageId"),rs.getInt("messageType"),rs.getString("message"),rs.getString("like"),rs.getString("dislike"),rs.getString("laugh"), rs.getString("date"),rs.getInt("replyId")))+";";
                }

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages.length()>1?messages.substring(0,messages.length()-1):"";
    }


    public String getEncryption(int userId,int chatId){
        String sql2 = "SELECT type,subserver FROM chatIds WHERE chatId= "+chatId;
        int type = 0;
        String subservers="";
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql2);
            if(rs.next()){
                type = rs.getInt("type");
                subservers = rs.getString("subserver");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(type==1){
            chatId = Integer.parseInt(subservers.split(",")[0]);
        }

        String[] chatIds = new String[0];
        String sql = "SELECT chatIds FROM USERS WHERE id= "+userId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) chatIds = rs.getString("chatIds").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        int messageId = 0;
        String message ="";
        for (int i = 0; i < chatIds.length; i++) {
            if(Integer.parseInt(chatIds[i].split(":")[0])==chatId){
                messageId = Integer.parseInt(chatIds[i].split(":")[1]);
            }
        }
        if(messageId==0) System.out.println("you are not oozv in this chat");
        else{
            message = getMessage(chatId,messageId).message;
        }
        return message;
    }
    public int getStats(int userId){
        String sql = "SELECT status FROM USERS WHERE id= "+userId;
        int stats=-1;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) stats = rs.getInt("status");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return stats;
    }
    public int getType2(int chatId){
        int type = 0;
        String sql =  "SELECT type FROM chatIds WHERE chatId= "+chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) {
                    type = rs.getInt("type");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return type;
    }
    public int getFriendFromChat(int chatId,int id){

        int friend = 0;
        String[] users = new String[0];
        String sql = "SELECT users FROM chatIds WHERE chatId= "+chatId;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) users = rs.getString("users").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < users.length; i++) {
            if(Integer.parseInt(users[i])!=id){
                friend = Integer.parseInt(users[i]);
                break;
            }
        }
        return friend;

    }
    public String getUserNameOfPrvChat(int id, int chatId){
        String username = "";
        String[] users = new String[0];
        String sql = "SELECT users FROM chatIds WHERE chatId= "+id;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) users = rs.getString("users").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(Integer.parseInt(users[0])==id){
            username = getUserNameFromId(Integer.parseInt(users[1]));
        }
        else if(Integer.parseInt(users[1])==id){
            username = getUserNameFromId(Integer.parseInt(users[0]));
        }else{
            System.out.println("user is not in chat");
        }
        return username;
    }
    public String getFriends(int id){
        String[] chats = new String[0];
        String sql = "SELECT chatIds FROM USERS WHERE id= "+id;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) chats = rs.getString("chatIds").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String friends = "";
        if(!chats[0].equals("")){
        for (int i = 0; i < chats.length; i++) {
            if(getType2(Integer.parseInt(chats[i].split(":")[0]))==2){
                friends += Integer.parseInt(chats[i].split(":")[0])+":"+getUserNameFromId(getFriendFromChat(Integer.parseInt(chats[i].split(":")[0]),id))+":"+getStats(getFriendFromChat(Integer.parseInt(chats[i].split(":")[0]),id))+",";
            }
        }}
        return friends.length()>0?friends.substring(0,friends.length()-1):"";
    }
    public String getChatUsers(int id){
        String[] chats = new String[0];
        String sql = "SELECT users FROM chatIds WHERE chatId= "+id;
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()) chats = rs.getString("users").split(",");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String friends = "";
        for (int i = 0; i < chats.length; i++) {
                friends += (getUserNameFromId(Integer.parseInt(chats[i].split(":")[0])))+": "+getStats(Integer.parseInt(chats[i].split(":")[0]))+",";
        }
        return friends;
    }
    public void ChangeChannelName(int id,int chatId,String newName){
        if(canUserchanegServerName(chatId,id)){
        String query = "UPDATE chatIds SET name=? WHERE chatId="+chatId;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, newName);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }
        }
    }
    public String showPins(int chatId){
        String sql = "SELECT pins FROM chatIds WHERE chatId = "+chatId;
        String pins = "";
        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            //System.out.println(rs.getString("password")+);
            if(rs.next()) pins = rs.getString("pins");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return pins;
    }
    public void changeStats(int id,int stats){
        String query = "UPDATE USERS SET status=? WHERE id="+id;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, stats);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }

    }
    public void changeUsername(int id,String newUserName){
        String query = "UPDATE USERS SET userName=? WHERE id="+id;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, newUserName);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }
    }
    public void changeEmail(int id,String newEmail){
        String query = "UPDATE USERS SET email=? WHERE id="+id;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, newEmail);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }
    }
    public void changePhoneNumber(int id,String newPhoneNumber){
        String query = "UPDATE USERS SET phoneNumber=? WHERE id="+id;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, newPhoneNumber);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }
    }
    public void changePassword(int id,String newPassword){
        String query = "UPDATE USERS SET password=? WHERE id="+id;
        try (Connection conn = connect();){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, Functions.sha3(newPassword));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public void blockChat(int id,int chatId){
        int type = 0;
        if(isUserInChat(id,chatId) && getType2(chatId)==2){

        if(getPVCreator(chatId)==id){//age creator block kard -3
          type  = -3;
        }else{
            type = -4;
        }
            String query = "UPDATE chatIds SET type=? WHERE chatId="+chatId;
            try (Connection conn = connect();){
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, type);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getErrorCode());
            }
    }
    }
    public String getBlocks(int id){
        String[] servers = getServers2(id).split(",");
        String blockedChats = "";
        if(!servers[0].equals("")){
        for (int i = 0; i < servers.length; i++) {
            String[] server = servers[i].split(":");
            int serverId = Integer.parseInt(server[0]);
            if(getType2(serverId)==-4 || getType2(serverId)==-3){
                System.out.println("tst36 "+serverId);
                if((id == getPVCreator(serverId) && getType2(serverId)==-3)||(id !=getPVCreator(serverId) && getType2(serverId)==-4)){
                    blockedChats+=serverId+":"+getUserNameFromId(getFriendFromChat(serverId,id));
                }
            }
        }
        }
        return blockedChats;
    }
    public void unBlock(int id,int chatId){
        if((id == getPVCreator(chatId) && getType2(chatId)==-3)||(id !=getPVCreator(chatId) && getType2(chatId)==-4)){
            String query = "UPDATE chatIds SET type=? WHERE chatId="+chatId;
            try (Connection conn = connect();){
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, 2);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getErrorCode());
            }
        }
    }
    public void unsendFriendReq(int id,int chatId){
        if((id == getPVCreator(chatId) && getType2(chatId)==-1)){
            String query = "UPDATE chatIds SET type=? WHERE chatId="+chatId;
            try (Connection conn = connect();){
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, -5);
                statement.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getErrorCode());
            }
        }
    }
}