package com.revature.bank;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 7645672315413153425L;

    private String userName;
    private String passWord;
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

    @Override
    public String toString(){
        String str;
        str = "\n----------Profile Info----------\n";
        str = str + "Username: " + this.userName +
                "\nPassword: " + this.passWord + "\n";
        str = str + "Accounts(s):\n";
        for(int i = 0; i < idList.size(); i++){
            str = str + "\t" + idList.get(i).toString() + "\n";
        }
        str = str + "--------------------------------\n";
        return str;

    }

    public void addID(UUID id){
        idList.add(id);
    }

    public ArrayList<UUID> getIdList() {
        return idList;
    }
}
