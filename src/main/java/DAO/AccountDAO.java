package DAO;
import java.sql.*;
import Model.Account;
import java.sql.Connection;
import Util.ConnectionUtil;

    public class AccountDAO {
     
    
    
        /**
         * Inserts a new account into the database.
         *
         * @param account The account to be created.
         * @return The created account with the generated account_id.
         */
        public Account insertAccount(Account account) {
            Connection conn = ConnectionUtil.getConnection();
            try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS); 
                stmt.setString(1, account.getUsername());
                stmt.setString(2, account.getPassword());
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int generate_key = (int)rs.getLong(1);
                    return new Account(generate_key,account.getUsername(),account.getPassword());  
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    
        /**
         * Retrieves an account by username.
         *
         * @param username The username to search for.
         * @return An Optional containing the account if found.
         */
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
                        rs.getString("password") // Consider hashing passwords in production
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

       
    }
