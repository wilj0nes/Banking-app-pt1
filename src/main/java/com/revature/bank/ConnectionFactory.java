package com.revature.bank;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static ConnectionFactory cf = null;

    private ConnectionFactory(){}

    public static synchronized ConnectionFactory getInstance(){
        if (cf==null){
            cf = new ConnectionFactory();
        }
        return cf;
    }

    public Connection getConnection(){
        Connection conn = null;
        try {
            Properties prop = new Properties();
            prop.load(new FileReader("src/main/resources/database.properties"));
            Class.forName(prop.getProperty("driver"));
            conn = DriverManager.getConnection(
                    prop.getProperty("url"),
                    prop.getProperty("usr"),
                    prop.getProperty("psw"));
            System.out.println(conn);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
