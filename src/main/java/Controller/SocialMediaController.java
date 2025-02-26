package Controller;

import java.util.logging.Handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import DAO.MessageDAO;
import Service.MessageService;
import Model.Account;
import Model.Message;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.apibuilder.EndpointGroup;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
      AccountService accountService;
      MessageService messageService;


      public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService((new MessageDAO()));
      }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postCreateMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessageByUser);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
       

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);  //Convert JSON into Java Object
        Account addedaccount = accountService.registerAccount(account); 
        if(addedaccount == null){
            ctx.status(400);
        }else{
            ctx.status(200).json(addedaccount);  //Return Status code 200 with JSON Response
        }
    }

    public void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginRequest = mapper.readValue(ctx.body(), Account.class);  //Convert JSON into Java Object 
        Account authenticatedAccount = accountService.authenticate(loginRequest);
        if (authenticatedAccount != null) {
            ctx.status(200).json(authenticatedAccount);  //Return Status code 200 with JSON Response
        } else {
            ctx.status(401);
        }
    }
       

    private void postCreateMessage(Context ctx) throws JsonProcessingException {
       ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);  //Convert JSON into Java Object
        Message createdMessage = messageService.createMessage(message);
         if (createdMessage != null) {
            ctx.status(200).json(createdMessage);  //Return Status code 200 with JSON Response
        } else {
            ctx.status(400);
        }
    }


    public void getAllMessages(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.retrieveAllMessages();
        ctx.status(200).json(messages);  //Return Status code 200 with JSON Response  
    }


    public void getMessageById(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));  //Converts message_id from ctx object to integer
        Message message = messageService.retrieveMessageById(messageId);
        if (message != null) {
            ctx.json(message);  
        } else {
            ctx.status(200).json(""); 
        }
    }

    public void getMessageByUser(Context ctx) throws JsonProcessingException{
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));   //Converts account_id from ctx object to integer
        List<Message> messages = messageService.retrieveMessagesByUser(accountId);
        ctx.json(messages); 
    }

    public void deleteMessage(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));   //Converts message_id from ctx object to integer
        Message deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage != null) {
            ctx.json(deletedMessage).status(200);
        } else {
            ctx.status(200);
        }
    } 

    public void updateMessage(Context ctx) throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));   //Converts message_id from ctx object to integer
        Message updatedMessage = ctx.bodyAsClass(Message.class);
        Message result = messageService.updateMessage(messageId, updatedMessage.getMessage_text());
        if (result != null) {
            ctx.json(result).status(200);
        } else {
            ctx.status(400); 
        }
    }
         
}


        
    

 
    


    
     

        



       



