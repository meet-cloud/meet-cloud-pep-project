package Service;
import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

public class MessageService {
    private  MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }
    

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            return null; // Validation: message must not be empty or exceed 255 characters
        }

        if (!accountDAO.userIdExists(message.getPosted_by())) {
            return null;
        }
        return messageDAO.createMessage(message);
    }
    
}
