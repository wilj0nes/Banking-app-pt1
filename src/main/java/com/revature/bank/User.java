package com.revature.bank;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 7645672315413153425L;

    private String userName;
    private String passWord;
    private ArrayList<UUID> idList;
    private int id;



    public void insertUser(String user, String pass){
        System.out.println("insert user: " + user);
        //TODO check if insert was successful
        //TODO make sure every thing commits

        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "INSERT INTO USERS VALUES (" +
                    "1,'" + user + "', '" + pass + "')";


            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs.getString("USERNAME"));


//            this.setUserName(rs.getString("USERNAME"));
//            this.setPassWord(rs.getString("PASSWORD"));
//            this.setId(rs.getInt("ACCOUNT_ID"));


            stmt.close();
        }
        catch (SQLException e) {
            //e.printStackTrace(); //TODO so something about this eventually
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

    }



    @SuppressWarnings("Duplicates")
    public User returnUser(int n){
        User u = new User();
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "SELECT * FROM USERS" +
                    "WHERE USER_ID = " + n;
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                u.id = rs.getInt("USER_ID");
                u.userName = rs.getString("USERNAME");
                u.passWord = rs.getString("PASSWORD");
            }

            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return u;
    }


    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int n){
        this.id = n;
    }

    public void setPassWord(String s){
        this.passWord = s;
    }
    public String getPassWord() {
        return passWord;
    }


    @Override
    public String toString(){
        String str;
        str = "\n----------Profile Info----------\n";
        str = str + "Username: " + this.userName +
                "\nPassword: " + this.passWord + "\n" +
                "ID: " + this.id + "\n";
//        str = str + "Accounts(s):\n";
//        for(int i = 0; i < idList.size(); i++){
//            str = str + "\t" + idList.get(i).toString() + "\n";
//        }
        str = str + "--------------------------------\n";
        return str;
    }



    public ArrayList<UUID> getIdList() {
        return idList;
    }


}
