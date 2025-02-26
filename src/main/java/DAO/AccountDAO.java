package DAO;
import java.sql.*;
import Model.Account;
import java.sql.Connection;
import Util.ConnectionUtil;

    public class AccountDAO {
     
    
    
        //  To Insert New Account:

        public Account insertAccount(Account account) {
            Connection conn = ConnectionUtil.getConnection();
            try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS); 
                stmt.setString(1, account.getUsername());
                stmt.setString(2, account.getPassword());
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();  //Get generated keys for Account_id
                if (rs.next()) {
                    int generate_key = (int)rs.getLong(1);
                    return new Account(generate_key,account.getUsername(),account.getPassword());  
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    

        // To Validate Username:

        public boolean usernameExists(String username) {
            Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT COUNT(*) FROM account WHERE username = ?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }


        //Login With Username and Password:

        public Account getAccountByUsernameAndPassword(String username, String password) {
            Connection conn = ConnectionUtil.getConnection();
            try{
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
    
                if (rs.next()) {
                    return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password") 
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

       
    }
