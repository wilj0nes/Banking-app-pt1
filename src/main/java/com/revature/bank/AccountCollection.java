package com.revature.bank;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class AccountCollection extends CollectionHolder implements Serializable {

    private static final long serialVersionUID = -5726690022975290484L;
    private transient final Logger logger = LogManager.getLogger(AccountCollection.class);

    private ArrayList<Account> accountList;

    public AccountCollection() {
        //accountList = new ArrayList<>();
    }

    public Account returnAccount(UUID id){
        for(int i = 0; i < accountList.size(); i++){
            if(accountList.get(i).getId() == id){
                return accountList.get(i);
            }
        }
        return null;
    }

    public Account createAccount(User user){
        UUID uuid = generateUUID();
        Account a = new Account(user, uuid);
        accountList.add(a);
        user.addID(uuid);
        System.out.println("New account: " + uuid);
        return a;
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

    //TODO test this
    // only called by CustomerCollection
    public void deleteUnusedAccounts(ArrayList<UUID> idList){
        for(int i = 0; i < idList.size(); i++){
            for(int j = 0; j < accountList.size(); j++){
                if(idList.get(i) == accountList.get(j).getId() && accountList.get(j).getUserList().size() <= 1){
                    this.deleteAccount(accountList.get(j));
                }
            }
        }
    }

    public void deleteAccount(Account a){
        //a.removeOwner();
        this.accountList.remove(a);
        //TODO this needs to remove UUID from user
    }
    public UUID generateUUID(){
        return UUID.randomUUID();
    }

}
