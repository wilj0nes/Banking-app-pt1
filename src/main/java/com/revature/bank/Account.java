package com.revature.bank;

import java.io.Serializable;
import java.util.UUID;

public class Account implements Serializable {
    private static final long serialVersionUID = 2582317066408575948L;

    private User[] users;
    private UUID id;

    private float balance;
    private int maxLength = 0;
    private int accountNumber;

    public User[] getUsers() {
        return users;
    }

    public User returnUser(String name, String pass){
        for(int i = 0; i < users.length; i++){
            if(users[i].getUserName().equals(name) &&
               users[i].getPassWord().equals(pass)){

                return users[i];
            }
        }
        return null;
    }

    public Account(User u, UUID id){
        users = new User[maxLength];
        setId(id);
        addUser(u);
    }

    public void addUser(User user){

        maxLength++;

        User[] tempArr = new User[maxLength];
        for(int i = 0; i < users.length; i++){
            tempArr[i] = users[i];
        }

        users = new User[maxLength];
        users = tempArr;
        users[maxLength - 1] = user;
        users[maxLength - 1].addID(this.getId());

//        for(int i = 0; i < maxLength; i++){
//            if(users[i] != null){
//                users[i] = u;
//            }
//        }
    }

    public float deposit(float f){
        balance = balance + f;
        return balance;
    }

    public float withdraw(float f){
        balance = balance - f;
        return balance;
    }

//    @Override
//    public String toString(){
//        return "[Username: " + this.userName + ", " +
//                "Password: " + this.passWord + ", " +
//                "ID: " + this.id + "]";
//
//
//    }


    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setId(UUID uuid){
        this.id = uuid;
    }

    public UUID getId() {
        return id;
    }



}
