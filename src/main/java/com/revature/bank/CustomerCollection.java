package com.revature.bank;

import java.io.Serializable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.ArrayList;

public class CustomerCollection extends CollectionHolder implements Serializable {

    //TODO rename this to UserCollection

    private static final long serialVersionUID = -6250939837132126354L;
    private transient final Logger logger = Logger.getRootLogger();

    private int maxLength = 0;
    private int currentIndex = 0;
    //private User[] users;
    private ArrayList<User> userList;

    public CustomerCollection(){
        userList = new ArrayList<>();
    }

    public void addUser(String name, String pass){
        User u = new User(name, pass);
        userList.add(u);
        logger.trace("User: " + u.getUserName() + ", has been added to the userList");
    }

    public User checkUserAndPass(String user, String pass){

        for(int i = 0; i < userList.size(); i++){
            System.out.println();
            if(userList.get(i).getUserName().equals(user) &&
                    userList.get(i).getPassWord().equals(pass)){
                return userList.get(i);
            }
        }
        return null;
    }

    //TODO test this
    public void deleteUser(User u, AccountCollection accounts){
        accounts.deleteUnusedAccounts(u.getIdList());
        userList.remove(u);
        logger.trace("User: '" + u.getUserName() + "' has been deleted");
    }

    public ArrayList<User> getUserList() {
        return this.userList;
    }

    @Override
    public String toString(){
        String str = "";
        for(int i = 0; i < userList.size(); i++){
            str = str + userList.get(i).getUserName() + ", ";
        }
        return str;
    }

    public ArrayList<User> returnAllUsers(){
        return this.userList;
    }

    public int getMaxLength() {
        return maxLength;
    }
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}


