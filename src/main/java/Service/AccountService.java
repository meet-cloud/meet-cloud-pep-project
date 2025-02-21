package Service;

import DAO.AccountDAO;
import Model.Account;
import java.sql.SQLException;

public class AccountService{
    
    private  AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO(null);
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO =  accountDAO;
    }

    public Account registerAccount(Account account){

        if(account.getPassword() == null || account.getPassword().length() < 4){
            return null;
        }
        if(account.getUsername() == null){
            return null;
        }
        if(accountDAO.usernameExists(account.getUsername())){
            return null;
        }
        return accountDAO.insertAccount(account);
    }
}
