package com.revature.bank;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 7645672315413153425L;

    private String userName;
    private String passWord;
    private boolean approved = false;
    private ArrayList<UUID> idList;

    public User(String user, String pass) {
        idList = new ArrayList<>();
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
        return "[" + this.getUserName() + ", " +
                     this.getPassWord() + "]";
    }

    public void addID(UUID id){
        idList.add(id);
    }

}
