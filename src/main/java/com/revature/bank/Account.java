package com.revature.bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Account implements Serializable {
    private static final long serialVersionUID = 2582317066408575948L;

    //private User[] users;
    private ArrayList<User> userList;
    private UUID id;

    private float balance;
    private int maxLength = 0;
    private int accountNumber;

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

    @Override
    public String toString(){
        return "[" + this.id + ", " + this.balance + "]";
    }

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
