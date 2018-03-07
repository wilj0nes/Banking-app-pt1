package com.revature.bank;

import java.io.Serializable;


public class Admin implements Serializable{
    private static final long serialVersionUID = -8328807712281843559L;

    private String userName = "admin";
    private String passWord = "p";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}

