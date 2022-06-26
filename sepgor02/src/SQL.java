
import com.google.gson.Gson;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class SQL implements DataManagement {
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

    public int insertUser(String userName, String passWord,String email) {
        String sql = "INSERT INTO USERS(userName,password,email,status,date) VALUES(?,?,?,?,?)";
        int error = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, Functions.sha3(passWord));
            pstmt.setString(3, email);
            pstmt.setInt(4, 4);
            pstmt.setString(5, Functions.getTimeStamp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            error = e.getErrorCode();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
    public boolean isUserInChat(int id,int chatId){
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
    public int addMessage(int chatId,int senderId,Message message){
        int error =0;
        int messageId = getMessageNumber(chatId)+1;
        int messageType=1;
        ///check type
        if(message instanceof TextMessage) messageType =1;

        String sql = "INSERT INTO 'messages' (chatId,messageId,senderId,messageType,message,date) VALUES(?,?,?,?,?,?)";
        if(isUserInChat(senderId,chatId)){
            try (Connection conn = connect();
                      PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, chatId);
                pstmt.setInt(2, messageId);
                pstmt.setInt(3, senderId);
                pstmt.setInt(4, messageType);
                pstmt.setString(5,(new Gson()).toJson(message));
                pstmt.setString(6,Functions.getTimeStamp());
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
    public int addChatToUser(int id, int chatId){
        int error =0;
        String chats =getChatsofUser(id);
        String query = "UPDATE USERS SET chatIds=? WHERE id="+id;

        try (Connection conn = connect();){
             PreparedStatement statement = conn.prepareStatement(query);
             statement.setString(1, chats+chatId);
             statement.executeUpdate();
        }
     catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            error = e.getErrorCode();
        }
        return error;
    }
    public int createChat(int id1,int id2){
        String sql = "INSERT INTO ChatIds(users,messagesNumber) VALUES(?,?)";
        int error = 0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ""+id1+","+id2);
            pstmt.setInt(2, 0);
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            int id=0;
            if (rs.next()) {
                id = Integer.parseInt(rs.getString(1));
            }
            addChatToUser(id1,id);
            addChatToUser(id2,id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            error = e.getErrorCode();
        }
        return error;
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
        String sql = "SELECT message,senderId,messageType,date FROM 'messages' WHERE chatId = "+chatId+" AND messageId ="+messageId;
        ClientMessage message=null;
        try {
            Connection conn = connect();

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            if(rs.next()){

                if(rs.getInt("messageType")==1) {
                    message = new ClientMessage((new Gson()).fromJson(rs.getString("message"), TextMessage.class), rs.getInt("senderId"), rs.getString("date"));
                }

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

}