package com.revature.bank;

import java.io.Serializable;

public class User extends CustomerCollection implements Serializable {
    private static final long serialVersionUID = 7645672315413153425L;

    private String userName;
    private String passWord;
    private boolean approved = false;

    public User(String user, String pass) {
        this.setUserName(user);
        this.setPassWord(pass);
    }

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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString(){
        return "[Username: " + this.userName + ", " +
                "Password: " + this.passWord + ", " +
                "Approved: " + this.isApproved() + "]";

    }

}
