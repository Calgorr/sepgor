
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class SQL implements DataManagement {
    //static Connection conn = null;
    private static Connection connect() {
        // SQLite connection string
        Connection conn = null;
        if(conn!=null) return conn;
        String url = "jdbc:sqlite:C:\\Users\\NP\\IdeaProjects\\sepgor02\\sepgor02.db";
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
    public int insertSession(User user, String session) {
       int id = user.getId();
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

}