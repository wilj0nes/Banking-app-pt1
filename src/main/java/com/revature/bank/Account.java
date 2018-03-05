package com.revature.bank;


import com.sun.org.apache.xpath.internal.operations.Bool;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Account extends AccountCollection implements Serializable {
    private static final long serialVersionUID = 2582317066408575948L;

    private User[] owners;
    private float balance;


    public float deposit(float f){
        balance = balance + f;
        return balance;
    }
    public float withdraw(float f){
        balance = balance - f;
        return balance;
    }


}
