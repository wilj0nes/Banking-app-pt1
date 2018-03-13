package com.revature.bank;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class Account implements Serializable {
    private static final long serialVersionUID = 2582317066408575948L;

    public boolean approved = false;
    public float balance;
    public int id;
    public String type;
    public String status;


//    public Account(int id){
//        insertAccount(id);
//    }

    public void insertAccount(int userID){
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();

            String sql = "INSERT INTO ACCOUNTS VALUES (1, 10.0, 'PENDING APPROVAL', 'N/A')";

            //TODO make sure every thing commits
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            stmt.close();
            //TODO insert into joint table using the user id;
        }
        catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public void searchAccounts(int id){
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
            String sql = "SELECT * FROM" + " ACCOUNTS WHERE ACCOUNT_ID = " + id;
            //TODO make sure every thing commits
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                this.id = rs.getInt("ACCOUNT_ID");
                this.balance = rs.getFloat("BALANCE");
                this.type = rs.getString("TYPE");
                this.status = rs.getString("STATUS");
            }

            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }



    public String getType(){
        return this.type;
    }
    public void setType(String s){
        this.type = s;
    }


//    @SuppressWarnings("Duplicates")
//    public void deposit(float f, int id){
//        balance = balance + f;
//
//        try {
//            Connection conn = ConnectionFactory.getInstance().getConnection();
//            String sql = "UPDATE TABLE ACCOUNTS SET BALANCE = " + this.balance + " WHERE ACCOUNT_ID = " + this.id;
//            Statement stmt = conn.createStatement();
//
//            ResultSet rs = stmt.executeQuery(sql);
//            stmt.close();
//        }
//        catch (SQLException e) {
//            //e.printStackTrace();
//        }
//        catch (NullPointerException e){
//            e.printStackTrace();
//        }
//    }

    //@SuppressWarnings("Duplicates")
//    public void withdraw(float f){
//        balance = balance - f;
//
//        try {
//            Connection conn = ConnectionFactory.getInstance().getConnection();
//            String sql = "UPDATE TABLE ACCOUNTS SET BALANCE = " + this.balance + " WHERE ACCOUNT_ID = " + this.id;
//            //TODO make sure every thing commits
//            Statement stmt = conn.createStatement();
//
//            ResultSet rs = stmt.executeQuery(sql);
//
////            while(rs.next()){
////                this.id = rs.getInt("ACCOUNT_ID");
////                this.balance = rs.getFloat("BALANCE");
////                this.type = rs.getString("TYPE");
////                this.status = rs.getString("STATUS");
////            }
//
//            stmt.close();
//        }
//        catch (SQLException e) {
//            //e.printStackTrace();
//        }
//        catch (NullPointerException e){
//            e.printStackTrace();
//        }
//    }
    public float getBalance(){
        return this.balance;
    }

//    public ArrayList<User> getUserList() {
//        return userList;
//    }

    public boolean getApproval(){
        return this.approved;
    }

    @Override
    public String toString(){
        String str;
        str = "\n----------Account Info----------\n";
        str = str + "ID: " + this.id + ", " +
                "\nBalance: " + this.balance + "\n" +
                "Status: " + this.status + "\n" +
                "Type: " + this.type + "\n";
//        for(int i = 0; i < userList.size(); i++){
//            str = str + "User(s): " + userList.get(i).getUserName() + ", ";
//        }
        str = str + "\n--------------------------------\n";
        return str;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int n){
        this.id = n;
    }


    //TODO implment approved
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
