package com.revature.bank;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 7645672315413153425L;

    private String userName;
    private String passWord;
    private boolean approved = false;
    private UUID[] IDs;              // accounts array
    private int IDlength = 0;

    public User(String user, String pass) {
        IDs = new UUID[IDlength];
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
        String str;

        if(this.IDs != null){
            str = "[Username: " + this.userName + ", " +
                    "Password: " + this.passWord +  ", " +
                    "ID: " + this.IDs + "]";
        }
        else{
            str = "[Username: " + this.userName + ", " +
                    "Password: " + this.passWord +  ", " +
                    "ID: " + "N/A" + "]";
        }
        return str;
    }

    public void addID(UUID id){

        IDlength++;

        UUID[] tempArr = new UUID[IDlength];
        for(int i = 0; i < IDs.length; i++){
            tempArr[i] = IDs[i];
        }
        IDs = new UUID[IDlength];
        IDs = tempArr;
        IDs[IDlength - 1] = id;
    }

}
