package Service;
import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;
import java.util.List;
import java.util.ArrayList;

public class MessageService {
    private MessageDAO messageDAO;
    


    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    

     public MessageService(){

     }


    public Message createMessage(Message message) {
        // Validation: message_text must not be blank or exceed 255 characters
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() ||
            message.getMessage_text().length() > 255) {
            return null;
        }

        boolean userExists;
        try {
         userExists = messageDAO.userIdExists(message.getPosted_by());
       } catch (RuntimeException e) {
           return null;  // Ensure we return null instead of causing 500 errors
        }
    
        if (!userExists) {
            return null; // Client error: user does not exist
        }

        

        // Save message to the database
        return messageDAO.createNewMessage(message);
    }


    public List<Message> retrieveAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message retrieveMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
}
