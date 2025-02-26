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
                return null;           }
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

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message";
            try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
            Message message = new Message(
            rs.getInt("message_id"),
            rs.getInt("posted_by"),
            rs.getString("message_text"),
            rs.getLong("time_posted_epoch"));
            messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return messages;
    }

    public Message getMessageById(int messageId) {
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, messageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return null; 
    }


    public List<Message> getMessagesByUser(int accountId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                messages.add(new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return messages; 
    }



    public Message deleteMessage(int messageId) {
        Connection conn = ConnectionUtil.getConnection();
        Message existingMessage = getMessageById(messageId);
        if (existingMessage == null) {
            return null;
        }
        String sql = "DELETE FROM message WHERE message_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, messageId);
            stmt.executeUpdate();
            return existingMessage; // Return the deleted message
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    public Message updateMessage(int messageId, String newMessageText) {
        Connection conn = ConnectionUtil.getConnection();
        Message existingMessage = getMessageById(messageId);
        if (existingMessage == null) {
            return null;
        }
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newMessageText);
            stmt.setInt(2, messageId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                existingMessage.setMessage_text(newMessageText);
                return existingMessage; }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

   
    
}
