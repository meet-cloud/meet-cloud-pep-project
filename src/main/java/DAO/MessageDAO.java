package DAO;

import java.sql.*;
import java.sql.Connection;
import Util.ConnectionUtil;
import Model.Message;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {


    public Message createNewMessage(Message message) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
    
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                return null; // Insertion failed
            }
    
            // Retrieve the generated message_id
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    return new Message(generatedId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error while inserting message", e);
        }
        
        return null;
    }
    

    public boolean userIdExists(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        try{
        Connection conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a user exists with this ID
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error while checking user existence", e);
           
        }
        
        
    }

   
    
}
