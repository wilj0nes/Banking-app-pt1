package com.revature.bank;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class User implements Serializable {
    private static final long serialVersionUID = 7645672315413153425L;

    private String userName;
    private String passWord;
    private ArrayList<UUID> idList;
    private int id;

    public User(String user, String pass) {
////        idList = new ArrayList<>();
////        this.setUserName(user);
////        this.setPassWord(pass);
        insertUser(user, pass);
    }

    public void insertUser(String user, String pass){
        //TODO check if insert was successful

        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "INSERT INTO USERS VALUES (" +
                            "1,'" + user + "', '" + pass + "')";
            //TODO make sure every thing commits
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

//            while(rs.next()){
//                System.out.println(rs);
//                System.out.println(rs);
//            }

            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    public String getUserName() {
        return userName;
    }



    public String getPassWord() {
        return passWord;
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
    public void deleteUnusedUUID(UUID id){
        this.idList.remove(id);
    }

    public void addID(UUID id){
        idList.add(id);
    }

    public ArrayList<UUID> getIdList() {
        return idList;
    }
}
