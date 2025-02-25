package Controller;

import java.util.logging.Handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Service.MessageService;
import Model.Account;
import Model.Message;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

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
        this.messageService = new MessageService();
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
       

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedaccount = accountService.registerAccount(account);
        if(addedaccount == null){
            ctx.status(400);
        }else{
            ctx.status(200).json(addedaccount);
        }
    }

    public void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginRequest = mapper.readValue(ctx.body(), Account.class); 
        Account authenticatedAccount = accountService.authenticate(loginRequest);
        if (authenticatedAccount != null) {
            ctx.status(200).json(authenticatedAccount);
        } else {
            ctx.status(401);
        }
    }
       

    private void postCreateMessage(Context ctx) throws JsonProcessingException {
       
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        
        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(400);
        }
    }
}
        
    

 
    


    
     

        



       



