package com.revature.bank;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class AccountCollection extends CollectionHolder implements Serializable {

    private static final long serialVersionUID = -5726690022975290484L;
    private transient final Logger logger = LogManager.getLogger(AccountCollection.class);

    private ArrayList<Account> accountList;

    public AccountCollection() {
        accountList = new ArrayList<>();
    }

    public Account returnAccount(UUID id){
        for(int i = 0; i < accountList.size(); i++){
            if(accountList.get(i).getId() == id){
                return accountList.get(i);
            }
        }
        return null;
    }

    public void createAccount(User user){
        UUID uuid = generateUUID();
        Account a = new Account(user, uuid);
        accountList.add(a);
        user.addID(uuid);
        System.out.println("New account: " + uuid);
        logger.debug(uuid + ", has been added to the accountList by " + user.getUserName());
    }

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    @Override
    public String toString(){
        String str = "";
        for(int i = 0; i < accountList.size(); i++){
            str = str + "[" + accountList.get(i).getId() + "]";
        }
        return str;
    }

    public UUID generateUUID(){
        return UUID.randomUUID();
    }

}


