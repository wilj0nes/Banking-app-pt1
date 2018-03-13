package com.revature.bank;

import sun.misc.JavaIOAccess;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;


public class Bank{

    public static void main(String args[]){
        try{
            BufferedReader br = new BufferedReader(
                    new FileReader("Welcome to.txt"));

            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){}
        System.out.println("\n");
        try{
            BufferedReader br = new BufferedReader(
                    new FileReader("bank.txt"));

            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){}
        UserInterface ui = new UserInterface();
        ui.choice(false);
    }
}
