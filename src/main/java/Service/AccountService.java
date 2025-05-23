package Service;

import DAO.AccountDAO;
import Model.Account;


public class AccountService{
    
    private  AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO =  accountDAO;
    }

    public Account registerAccount(Account account){

        //Validation for Empty Username and Password
        if(account.getPassword() == null || account.getPassword().length() < 4  || account.getUsername().isBlank()){
            return null;
        }
        //Validation For User to Exist
        if(accountDAO.usernameExists(account.getUsername())){
            return null;
        }
        return accountDAO.insertAccount(account);
    }

    
    public Account authenticate(Account account) {
        return accountDAO.getAccountByUsernameAndPassword(account.getUsername(),account.getPassword());
    }
}
