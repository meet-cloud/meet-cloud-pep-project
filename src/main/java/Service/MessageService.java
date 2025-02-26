package Service;
import Model.Message;
import DAO.MessageDAO;
import java.util.List;


public class MessageService {
    private MessageDAO messageDAO;
    
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }
     
    public MessageService(){}

      
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
           return null;  
        }
        if (!userExists) {
            return null; 
        }
        return messageDAO.createNewMessage(message);
    }


    public List<Message> retrieveAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message retrieveMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public List<Message> retrieveMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public Message updateMessage(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            return null; 
        }

        return messageDAO.updateMessage(messageId, newMessageText);
    }
}
