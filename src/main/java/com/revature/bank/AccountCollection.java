package com.revature.bank;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.UUID;

public class AccountCollection implements Serializable {

    private static final long serialVersionUID = -5726690022975290484L;
    private transient final Logger logger = LogManager.getLogger(AccountCollection.class);



    private int maxLength = 0;
    private Account[] accounts;
    private UUID id;

    public AccountCollection() {
        accounts = new Account[maxLength];
    }

    public void createAccount(User user){
        UUID uuid = generateUUID();
        Account a = new Account(user, uuid);
        maxLength++;

        Account[] tempArr = new Account[maxLength];
        for(int i = 0; i < accounts.length; i++){
            tempArr[i] = accounts[i];
        }

        accounts = new Account[maxLength];
        accounts = tempArr;
        accounts[maxLength - 1] = a;
        logger.trace("new account: " + accounts[maxLength - 1].getId() + ", User:" + user.getUserName());

        //user.setId(accounts[maxLength - 1].getId());
            // probably redundant
    }

    public Account[] getAccounts() {
        return accounts;
    }

    @Override
    public String toString(){
        String str = "";

        for(int i = 0; i < accounts.length; i++){
            str = str + "[" + accounts[i].getId() + "]";
        }
        return str;
    }

    public UUID generateUUID(){
        return UUID.randomUUID();
    }
    public int getMaxLength() {
        return maxLength;
    }

}


