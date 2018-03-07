package com.revature.bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Account implements Serializable {
    private static final long serialVersionUID = 2582317066408575948L;

    private ArrayList<User> userList;
    private UUID id;
    private boolean approved = false;
    private float balance;

    public Account(User u, UUID id){
        userList = new ArrayList<>();
        userList.add(u);
        setId(id);
    }

    public void addUser(User user){
        userList.add(user);
    }

    public float deposit(float f){
        balance = balance + f;
        return balance;
    }

    public float withdraw(float f){
        balance = balance - f;
        return balance;
    }
    public float getBalance(){
        return this.balance;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public boolean getApproval(){
        return this.approved;
    }

    @Override
    public String toString(){
        String str;
        str = "\n----------Account Info----------\n";
        str = str + "ID: " + this.id + ", " +
                  "\nBalance: " + this.balance + "\n" +
                    "Appproved: " + this.approved + "\n";
        for(int i = 0; i < userList.size(); i++){
            str = str + "User(s): " + userList.get(i).getUserName() + ", ";
        }
        str = str + "\n--------------------------------\n";
        return str;
    }


    public void setId(UUID uuid){
        this.id = uuid;
    }

    public UUID getId() {
        return id;
    }

    public ArrayList getOwnersList(){
        return this.userList;
    }

    public void removeOwner(User u){
        this.userList.remove(u);
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
