package com.revature.bank;

import java.io.Serializable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.ArrayList;

public class CustomerCollection extends CollectionHolder implements Serializable {

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

//        logger.trace("New user: " + users[maxLength - 1].toString());
//        for(int i = 0; i < users.length; i++){
////            logger.fatal(users[i].getUserName());
//            if(users[i] == null && !stop){
//                users[i] = u;
//                System.out.println("New User --> " + users[i]);
//                currentIndex = i;
//                stop = true;
//            }
//        }
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


