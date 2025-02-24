package DAO;
import java.sql.*;
import java.sql.Connection;
import Util.ConnectionUtil;
import Model.Message;

public class MessageDAO {
   public Message createMessage(Message message) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted) VALUES (?, ?, ?);";
             Connection conn = ConnectionUtil.getConnection();
        try{
             PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
            
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                return null; // No message was inserted
            }
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generate_key = (int)rs.getLong(1);
                return new Message(generate_key,message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
            } 
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
