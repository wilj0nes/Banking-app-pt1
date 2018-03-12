package com.revature.bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;


public class Bank{

    public static void main(String args[]){
        UserInterface ui = new UserInterface();
        ui.choice(false);
    }
}
